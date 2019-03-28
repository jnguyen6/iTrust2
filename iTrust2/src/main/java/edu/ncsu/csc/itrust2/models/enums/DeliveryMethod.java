package edu.ncsu.csc.itrust2.models.enums;

/**
 * Enum class for the DeliveryMethod
 *
 * @author Shukri Qubain (scqubain@ncsu.edu)
 *
 */
public enum DeliveryMethod {

    /**
     * Type vaginal
     */
    Vaginal ( "Vaginal" ),

    /**
     * Type cesarean
     */
    Cesarean ( "Cesarean" ),

    /**
     * Type miscarriage
     */
    Miscarriage ( "Miscarriage" ),;

    /** the type of delivery method */
    private String deliveryMethodType = "";

    /**
     * Constructs the Enum
     *
     * @param type
     *            the delivery method type
     */
    DeliveryMethod ( final String type ) {
        this.deliveryMethodType = type;
    }

    /**
     * Returns the delivery method type
     *
     * @return deliveryMethodType the delivery method type
     */
    public String getType () {
        return this.deliveryMethodType;
    }

    /**
     * Returns a string of the delivery method type
     */
    @Override
    public String toString () {
        return deliveryMethodType;
    }

}
