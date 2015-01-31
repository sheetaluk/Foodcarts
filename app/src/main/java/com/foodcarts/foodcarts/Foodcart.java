package com.foodcarts.foodcarts;

/**
 * Created by sheetaluk on 1/29/15.
 */
public class Foodcart {
    private String mLatitude;
    private String mLongitude;
    private String mApplicant;
    private String mAddress;
    private String mFooditems;

    public String getmLatitude() {
        return mLatitude;
    }

    public String getmLongitude() {
        return mLongitude;
    }

    public String getmApplicant() {
        return mApplicant;
    }

    public String getmAddress() {
        return mAddress;
    }

    public String getmFooditems() {
        return mFooditems;
    }

    public void setmLatitude(String mLatitude) {
        this.mLatitude = mLatitude;
    }

    public void setmLongitude(String mLongitude) {
        this.mLongitude = mLongitude;
    }

    public void setmApplicant(String mApplicant) {
        this.mApplicant = mApplicant;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public void setmFooditems(String mFooditems) {
        this.mFooditems = mFooditems;
    }

    @Override
    public String toString() {
        return "Foodcart{" +
                "mLatitude='" + mLatitude + '\'' +
                ", mLongitude='" + mLongitude + '\'' +
                ", mApplicant='" + mApplicant + '\'' +
                ", mAddress='" + mAddress + '\'' +
                ", mFooditems='" + mFooditems + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Foodcart foodcart = (Foodcart) o;

        if (mAddress != null ? !mAddress.equals(foodcart.mAddress) : foodcart.mAddress != null)
            return false;
        if (mApplicant != null ? !mApplicant.equals(foodcart.mApplicant) : foodcart.mApplicant != null)
            return false;
        if (mFooditems != null ? !mFooditems.equals(foodcart.mFooditems) : foodcart.mFooditems != null)
            return false;
        if (mLatitude != null ? !mLatitude.equals(foodcart.mLatitude) : foodcart.mLatitude != null)
            return false;
        if (mLongitude != null ? !mLongitude.equals(foodcart.mLongitude) : foodcart.mLongitude != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mLatitude != null ? mLatitude.hashCode() : 0;
        result = 31 * result + (mLongitude != null ? mLongitude.hashCode() : 0);
        result = 31 * result + (mApplicant != null ? mApplicant.hashCode() : 0);
        result = 31 * result + (mAddress != null ? mAddress.hashCode() : 0);
        result = 31 * result + (mFooditems != null ? mFooditems.hashCode() : 0);
        return result;
    }
}
