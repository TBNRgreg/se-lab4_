package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GT4500Test {

    private GT4500 ship;

    @BeforeEach
    public void init() {
        this.ship = new GT4500();
    }

    @Test
    void fireTorpedo_Single_UsesPrimaryFirstAndAlternates() throws Exception {
        // Tesztelt követelmény:
        // A SINGLE módban a hajó először az első torpedó raktárból lő,
        // majd a következő lövésnél vált a második raktárra.
        // Előfeltételek: mindkét raktár tartalmaz legalább egy torpedót.
        ship = new GT4500(3, 0.0, 3, 0.0);

        // A teszt lépései:
        assertTrue(ship.fireTorpedo(FiringMode.SINGLE));
        assertEquals(2, getTorpedoCount(ship, "primaryTorpedoStore"));
        assertEquals(3, getTorpedoCount(ship, "secondaryTorpedoStore"));

        assertTrue(ship.fireTorpedo(FiringMode.SINGLE));
        assertEquals(2, getTorpedoCount(ship, "primaryTorpedoStore"));
        assertEquals(2, getTorpedoCount(ship, "secondaryTorpedoStore"));

        assertTrue(ship.fireTorpedo(FiringMode.SINGLE));
        assertEquals(1, getTorpedoCount(ship, "primaryTorpedoStore"));
        assertEquals(2, getTorpedoCount(ship, "secondaryTorpedoStore"));
    }

    @Test
    void fireTorpedo_Single_WhenSelectedStoreIsEmpty_UsesTheOtherStore() throws Exception {
        // Tesztelt követelmény:
        // Ha a soron következő raktár üres, a rendszer a másik raktárból próbál lőni.
        // Előfeltételek: az első raktár üres, a második tartalmaz torpedót.
        ship = new GT4500(0, 0.0, 2, 0.0);

        // A teszt lépései:
        assertTrue(ship.fireTorpedo(FiringMode.SINGLE));
        assertEquals(0, getTorpedoCount(ship, "primaryTorpedoStore"));
        assertEquals(1, getTorpedoCount(ship, "secondaryTorpedoStore"));
    }

    @Test
    void fireTorpedo_Single_WhenBothStoresAreEmpty_ReturnsFalse() throws Exception {
        // Tesztelt követelmény:
        // Ha mindkét raktár üres, a hajó nem tud torpedót lőni.
        // Előfeltételek: mindkét raktár 0 torpedót tartalmaz.
        ship = new GT4500(0, 0.0, 0, 0.0);

        // A teszt lépései:
        assertFalse(ship.fireTorpedo(FiringMode.SINGLE));
        assertEquals(0, getTorpedoCount(ship, "primaryTorpedoStore"));
        assertEquals(0, getTorpedoCount(ship, "secondaryTorpedoStore"));
    }

    @Test
    void fireTorpedo_All_FiresBothNonEmptyStores() throws Exception {
        // Tesztelt követelmény:
        // Az ALL mód egyszerre próbálja meg mindkét raktárból kilőni a torpedót.
        // Előfeltételek: mindkét raktár tartalmaz torpedót.
        ship = new GT4500(2, 0.0, 3, 0.0);

        // A teszt lépései:
        assertTrue(ship.fireTorpedo(FiringMode.ALL));
        assertEquals(1, getTorpedoCount(ship, "primaryTorpedoStore"));
        assertEquals(2, getTorpedoCount(ship, "secondaryTorpedoStore"));
    }

    private static int getTorpedoCount(GT4500 ship, String fieldName) throws Exception {
        Field field = GT4500.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        TorpedoStore store = (TorpedoStore) field.get(ship);
        return store.getTorpedoCount();
    }
}
