package com.yonikal.tools;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Created by yoni on 16/08/2017.
 */
public class ContactPhone implements Comparable<ContactPhone>, Parcelable {

    public final static int PHONE_NUMBER__PREFIX = 3;
    public final static int PHONE_NUMBER = 7;
    public static final Parcelable.Creator<ContactPhone> CREATOR = new Parcelable.Creator<ContactPhone>() {
        @Override
        public ContactPhone createFromParcel(Parcel source) {
            return new ContactPhone(source);
        }

        @Override
        public ContactPhone[] newArray(int size) {
            return new ContactPhone[size];
        }
    };
    private String mNumber;
    private String mName;
    private Uri mUri;

    public ContactPhone() {
    }


    public ContactPhone(String name, String number, Uri uri) {
        mUri = uri;
        mName = name;
        mNumber = number;
    }

    public ContactPhone(String name, String number) {
        mName = name;
        mNumber = number;
    }

    protected ContactPhone(Parcel in) {
        mNumber = in.readString();
        mName = in.readString();
        mUri = in.readParcelable(Uri.class.getClassLoader());
    }

    public static List<ContactPhone> filterList(List<ContactPhone> contactList, String startWith) {
        if (contactList == null) contactList = new ArrayList<>();
        List<ContactPhone> returnList = new ArrayList<>();
        for (ContactPhone contact : contactList) {
            if (contact.getName().toLowerCase().contains(startWith.toLowerCase()) || contact.getNumber().toLowerCase().contains(startWith.toLowerCase())) {
                returnList.add(contact);
            }
        }
        Collections.sort(returnList, new SortFilterContactList(returnList, startWith));
        return returnList;
    }

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String mNumber) {
        this.mNumber = mNumber;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public Uri getUri() {
        return mUri;
    }

    public void setUri(Uri mUri) {
        this.mUri = mUri;
    }

    @Override
    public boolean equals(Object o) {
        return mNumber.equals(((ContactPhone) o).mNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mNumber);
        dest.writeString(this.mName);
        dest.writeParcelable(this.mUri, flags);
    }

    @Override
    public int compareTo(ContactPhone other) {
        int OTHER_BIGGER = -1;
        int OTHER_SMALLER = 1;
        if (other == null) {
            return OTHER_SMALLER;
        }
        if (mNumber == null && other.getNumber() != null) {
            return OTHER_BIGGER;
        }
        if (mNumber != null && other.getNumber() == null) {
            return OTHER_SMALLER;
        }

        return getName().compareToIgnoreCase(other.getName());
    }


    static class SortFilterContactList implements Comparator<ContactPhone> {
        private List<ContactPhone> filterList;
        private String filterSearchName;

        public SortFilterContactList(List<ContactPhone> filterList, String filterSearchName) {
            this.filterList = filterList;
            this.filterSearchName = filterSearchName;
        }

        @Override
        public int compare(ContactPhone t, ContactPhone t1) {
            int OTHER_BIGGER = -1;
            int OTHER_SMALLER = 1;
            if (t == null) {
                return OTHER_BIGGER;
            }
            if (t1 == null) {
                return OTHER_SMALLER;
            }

            if (t.getNumber() == null && t1.getNumber() != null) {
                return OTHER_BIGGER;
            }
            if (t.getNumber() != null && t1.getNumber() == null) {
                return OTHER_SMALLER;
            }
            if (t.getName().startsWith(filterSearchName) && !t1.getName().startsWith(filterSearchName)) {
                return OTHER_BIGGER;
            }
            if (t1.getName().startsWith(filterSearchName) && !t.getName().startsWith(filterSearchName)) {
                return OTHER_SMALLER;
            }


            return t.getName().compareToIgnoreCase(t1.getName());
        }
    }


    public static class SortFilterContactListNameBefore implements Comparator<ContactPhone> {
        private List<ContactPhone> filterList;
        private String filterSearchName;

        public SortFilterContactListNameBefore(List<ContactPhone> filterList) {
            this.filterList = filterList;
        }

        @Override
        public int compare(ContactPhone t, ContactPhone t1) {
            int OTHER_BIGGER = -1;
            int OTHER_SMALLER = 1;
            if (t == null) {
                return OTHER_BIGGER;
            }
            if (t1 == null) {
                return OTHER_SMALLER;
            }

            if (t.getNumber() == null && t1.getNumber() != null) {
                return OTHER_BIGGER;
            }
            if (t.getNumber() != null && t1.getNumber() == null) {
                return OTHER_SMALLER;
            }
            if (t.getName().matches("\\d+") && !t1.getName().matches("\\d+")) {
                return OTHER_SMALLER;
            }
            if (!t.getName().matches("\\d+") && t1.getName().matches("\\d+")) {
                return OTHER_BIGGER;
            }
            if (t.getName().matches("\\d+") && !t1.getName().matches("\\d+")) {
                return OTHER_SMALLER;
            }
            if (!t.getName().matches("\\d+") && t1.getName().matches("\\d+")) {
                return OTHER_BIGGER;
            }

            Collator coll = Collator.getInstance(new Locale("iw", "IL"));
            coll.setStrength(Collator.PRIMARY);
            return coll.compare(t.getName().toLowerCase(), t1.getName().toLowerCase());//t.getName().compareToIgnoreCase(t1.getName());
        }
    }


}