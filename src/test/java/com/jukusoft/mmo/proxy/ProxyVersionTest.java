package com.jukusoft.mmo.proxy;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProxyVersionTest {

    @Test
    public void testConstructor () {
        new ProxyVersion();
    }

    @Test
    public void testClientBuildNumber () {
        assertEquals(true, ProxyVersion.CURRENT_BUILD_VERSION > 0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testIsAccepted () {
        ProxyVersion.isAccepted(-1);
        assertEquals(false, ProxyVersion.isAccepted(0));
        assertEquals(false, ProxyVersion.isAccepted(-1));
    }

    @Test
    public void testIsAccepted1 () {
        assertEquals(false, ProxyVersion.isAccepted(0));
        assertEquals(true, ProxyVersion.isAccepted(1));
    }

}
