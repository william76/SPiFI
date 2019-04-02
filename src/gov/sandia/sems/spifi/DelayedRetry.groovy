#!/usr/bin/env groovy
/**
 * DelayedRetry.groovy
 *
 * DelayedRetry class used by SPiFI
 *
 * @author  William McLendon
 * @version 1.0
 * @since   2019-02-21
 */
package gov.sandia.sems.spifi

import gov.sandia.sems.spifi.interfaces.Printable as Printable

abstract class DelayedRetry
{
    // Class members
    public Integer retry_delay = 90
    public String  retry_delay_units = "SECONDS"

    private static _env

    // Constructor
    def DelayedRetry(Map params)
    {
        // Validate parameters
        if(!params.containsKey("env"))
        {
            throw new Exception("[SPiFI]> Missing required parameter: 'env'")
        }
        this._env = params.env

        // Handle optional parameters
        if(params.containsKey("retry_delay"))
        {
            assert params.retry_delay instanceof Integer
            this.retry_delay = params.retry_delay
            if(this.retry_delay < 0)
            {
                this.retry_delay = 0
            }
        }
        if(params.containsKey("retry_delay_units"))
        {
            assert params.retry_delay_units instanceof String
            assert params.retry_delay_units in ["HOURS", "MINUTES", "SECONDS"]
            this.retry_delay_units = params.retry_delay_units
        }
    }

    // Helpers/Utility
    String getDelayedRetryAsString() { return "retry_delay: ${this.retry_delay}, retry_delay_units: \"${this.retry_delay_units}\"" }

}   // class DelayedRetry


