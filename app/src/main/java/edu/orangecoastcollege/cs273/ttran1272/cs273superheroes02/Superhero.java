package edu.orangecoastcollege.cs273.ttran1272.cs273superheroes02;

/**
 * Created by ttran1272 on 10/24/2017.
 */

public class Superhero {
    private String mUserName;
    private String mName;
    private String mSuperpower;
    private String mOneThing;
    private String mFileName;

    /**
     * Instantiate a new <code>Superhero</code> given its username, name, superpower, and one special thing
     * @param userName User name of the superhero
     * @param name     First-name and initialized last-name of the superhero
     * @param superpower Super power of the student
     * @param oneThing One special thing of the student
     */
    public Superhero(String userName, String name, String superpower, String oneThing) {
        mUserName = userName;
        mName = name;
        mSuperpower = superpower;
        mOneThing = oneThing;
        mFileName = userName + ".png";
    }

    /**
     * Gets the user name of the student
     * @return the user name of the student
     */
    public String getUserName() {
        return mUserName;
    }

    /**
     * Gets the name of the student
     * @return The name of the student
     */
    public String getName() {
        return mName;
    }

    /**
     * Gets superpower of the student
     * @return The superpower of the student
     */

    public String getSuperpower() {
        return mSuperpower;
    }

    /**
     * Gets one special thing of the student
     * @return The special thing of the student
     */
    public String getOneThing() {
        return mOneThing;
    }

    /**
     * get the file name of the picture of the student.
     * @return The file name that contains a picture of the student
     */

    public String getFileName() {
        return mFileName;
    }

    /**
     * Compares two superheros for equality based on username, name, superpower, onething, and filename;
     * @param obj the other superhero
     * @return True if the superheros are the same, false otherwise.
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Superhero superhero = (Superhero) obj;

        if (!mUserName.equals(superhero.mUserName)) return false;
        if (!mName.equals(superhero.mName)) return false;
        if (!mSuperpower.equals(superhero.mSuperpower)) return false;
        if (!mOneThing.equals(superhero.mOneThing)) return false;

        return mFileName.equals(superhero.mFileName);
    }

    @Override
    public String toString() {
        return "Superhero{" + "Username='" + mUserName + '\'' +
                ", Name='" + mName + '\'' +
                ", Superpower='" + mSuperpower + '\'' +
                ", OneThing='" + mOneThing + '\'' + "}";
    }
}
