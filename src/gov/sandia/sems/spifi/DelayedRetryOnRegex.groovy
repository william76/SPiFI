#!/usr/bin/env groovy
/**
 * RetryDelayOnRegex.groovy
 *
 * RetryDelayOnRegex class used by SPiFI
 *
 * @author  William McLendon
 * @version 1.0
 * @since   2019-02-21
 */
package gov.sandia.sems.spifi

// Import Java modules
import java.util.regex.Matcher as Matcher

// Import JenkinsCI modules
import org.jenkinsci.plugins.workflow.support.steps.build.RunWrapper as jenkinsRunWrapper

// Import SPiFI modules
import gov.sandia.sems.spifi.DelayedRetry as DelayedRetry
import gov.sandia.sems.spifi.interfaces.Printable as Printable
import gov.sandia.sems.spifi.interfaces.ScanBuildLog as ScanBuildLog


/**
 * Specialization of DelayedRetry to include a REGEX condition.
 */
class DelayedRetryOnRegex extends DelayedRetry implements ScanBuildLog, Printable
{
    private static _env
    public String retry_regex = ""

    /**
     *  Constructor
     */
    DelayedRetryOnRegex(Map params)
    {
        // Call the c'tor of the superclass
        super(params)

        // Handle required parameters
        if(!params.containsKey("env"))
        {
            throw new Exception("[SPiFI]> Missing required parameter: 'env'")
        }
        this._env = params.env

        // Handle optional parameters
        if(params.containsKey("retry_regex"))
        {
            assert params.retry_regex instanceof String
            this.retry_regex = params.retry_regex
        }
    }


    /**
     * Scan the build log for a match of the 'retry' condition.
     *
     * Parameters:
     * build_log [REQUIRED] - List<String> containing the console log as a list of lines.
     *
     */
    Boolean scanBuildLog( Map params )
    {
        // Check Parameters
        if( !params.containsKey("build_log") )
            throw new Exception("[SPiFI]> INPUT ERROR: Missing required parameter 'build_log'")
        if( !params.build_log instanceof List<String> )
            throw new Exception("[SPiFI]> TYPE ERROR: Expected 'build_log' to be a List<String>.")

        List<String> build_log = params.build_log

        Boolean output = build_log.find
        { line ->
            // Check the line for matches to the regex
            if( (line =~ /${this.retry_regex}/).getCount() > 0 )
            {
                // Break the 'find' search if found
                return true
            }
            // Otherwise, continue
            return false
        }
        // output will be null if not true, so force it to true
        output = output == true

        return output
    }


    // Helpers/Utility
    String asString()
    {
        return "${this.getDelayedRetryAsString()}, retry_regex: \"${this.retry_regex}\""
    }

}   // class RetryDelayOnRegex

