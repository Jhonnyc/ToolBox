package com.yonikal.tools;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.v4.content.CursorLoader;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yoni on 16/08/2017.
 */
public class ContactsLoader {

    private static final String TAG = ContactsLoader.class.getSimpleName();
    private static ContactsLoader sContactsLoader = new ContactsLoader();
    private static String CLIENT_PHONE_NUMBER;
    private Activity mContext;
    private OnContactsLoaded mCallBack;
    private HashMap<String, ContactPhone> mAllContactsHashMap = new HashMap<>();

    private boolean mStartLoading = false;
    private boolean mFinishLoading = false;

    private ContactsLoader() {
        setPhoneNumber("");
    }

    public static ContactsLoader getInstance() {
        return sContactsLoader;
    }

    private void setPhoneNumber(String phoneNumber) {
        CLIENT_PHONE_NUMBER = phoneNumber;
    }

    public HashMap<String, ContactPhone> getAllContactsHashMap() {
        return mAllContactsHashMap;
    }

    public void loadContact(Activity context, OnContactsLoaded... callBack) {
        sContactsLoader.mContext = context;
        if (callBack != null && callBack.length > 0) {
            sContactsLoader.mCallBack = callBack[0];
        }
        new LoadContacts().execute();
    }

    public void loadAgain(Activity context, OnContactsLoaded... callBack) {
        mStartLoading = false;
        mFinishLoading = false;
        sContactsLoader.mContext = context;
        if (callBack != null && callBack.length > 0) {
            sContactsLoader.mCallBack = callBack[0];
        }
        new LoadContacts().execute();
    }

    public void getContacts(String... selectionArgs) {
        if (!mStartLoading && !mFinishLoading) {
            mStartLoading = true;
            mAllContactsHashMap.clear();

            Cursor phone;
            final String[] numberProjection = new String[]{
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.PHOTO_URI
            };
            if (selectionArgs != null && selectionArgs.length >= 1) {
                if (Looper.myLooper() == null) {
                    Looper.prepare();
                }
                String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " LIKE ? " + "OR " +
                        ContactsContract.CommonDataKinds.Phone.NUMBER + " LIKE ?";
                phone = new CursorLoader(mContext,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        numberProjection,
                        selection,
                        selectionArgs,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC").loadInBackground();
            } else {
                if (Looper.myLooper() == null) {
                    Looper.prepare();
                }
                phone = new CursorLoader(mContext,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        numberProjection,
                        null,
                        null,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC").loadInBackground();
            }


            if (phone.moveToFirst()) {
                final int contactNumberColumnIndex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                final int contactNameColumnIndex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                final int contactImageUriColumnIndex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI);

                while (!phone.isAfterLast()) {
                    final String number = phone.getString(contactNumberColumnIndex);
                    final String contactName = phone.getString(contactNameColumnIndex);
                    final String contactUri = phone.getString(contactImageUriColumnIndex);

                    if (!TextUtils.isEmpty(number)) {
                        String phoneNumber = number;
                        if (phoneNumber != null && !mAllContactsHashMap.containsKey(number) && !CLIENT_PHONE_NUMBER.equals(phoneNumber)) {
                            Uri uri;
                            if (contactUri == null) {
                                uri = null;
                            } else {
                                uri = Uri.parse(contactUri);
                            }
                            ContactPhone contactPhone = new ContactPhone(contactName, phoneNumber, uri);
                            mAllContactsHashMap.put(phoneNumber, contactPhone);
                        }
                    }
                    phone.moveToNext();
                }
            }
            phone.close();
            mFinishLoading = true;
        }
    }


    public ContactPhone getContactByNameAndPhoneNumber(String name, String phoneNumber) {
        if (mAllContactsHashMap == null || !mAllContactsHashMap.containsKey(phoneNumber)) {
            return new ContactPhone(name, phoneNumber);
        }
        return mAllContactsHashMap.get(phoneNumber);
    }


    public List<ContactPhone> getContactsAsList() {
        return new ArrayList<>(mAllContactsHashMap.values());
    }

    public interface OnContactsLoaded {
        void onContactLoadedFinish();
    }

    private class LoadContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            if (!mStartLoading && !mFinishLoading) {
                getContacts();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mCallBack != null) {
                mCallBack.onContactLoadedFinish();
            }
        }
    }

}