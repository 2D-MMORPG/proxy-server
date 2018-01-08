package com.jukusoft.mmo.proxy;

public class ProxyVersion {

    public static final int CURRENT_BUILD_VERSION = 1;

    //array with all supported versions. if version is not supported, client will be rejected
    public static final int[] ACCEPTED_CLIENT_VERSIONS = new int[]{1};

    /**
    * private constructor
    */
    protected ProxyVersion () {
        //
    }

    /**
    * check, if client build number is supported
     *
     * @param clientBuildNumber client build number
     *
     * @return true, if client version is supported
    */
    public static boolean isAccepted (int clientBuildNumber) {
        if (clientBuildNumber < 0) {
            throw new IllegalArgumentException("client build number has to be >= 0.");
        }

        for (int i = 0; i < ACCEPTED_CLIENT_VERSIONS.length; i++) {
            if (ACCEPTED_CLIENT_VERSIONS[i] == clientBuildNumber) {
                return true;
            }
        }

        return false;
    }

}
