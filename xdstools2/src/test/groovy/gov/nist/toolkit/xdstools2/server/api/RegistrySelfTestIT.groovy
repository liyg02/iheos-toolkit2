package gov.nist.toolkit.xdstools2.server.api

import gov.nist.toolkit.installation.Installation
import gov.nist.toolkit.results.client.Result
import spock.lang.Specification

/**
 * Created by bill on 9/29/15.
 */
class RegistrySelfTestIT extends Specification {
    ToolkitApi api;
    String patientId = 'BR14^^^&1.2.360&ISO'

    def setup() {
        api = new ToolkitApi()
        println "EC is ${Installation.installation().externalCache().toString()}"
        println "${api.getSiteNames(true)}"
    }

    def 'Submit Pid transaction to Registry simulator'() {
        when:
        String testSession = null;  // use default
        String siteName = 'mike__reg'
        String testName = "15804"
        List<String> sections = new ArrayList<>()
        sections.add("section")
        Map<String, String> params = new HashMap<>()
        params.put('$patientid$', patientId)     // this is igored - a new patient id is created
        boolean stopOnFirstError = true

        and: 'Run pid transaction test'
        List<Result> results = api.runTest(testSession, siteName, testName, sections, params, stopOnFirstError)

        then:
        true
        results.size() == 1
        results.get(0).passed()
    }



    def 'Run 11990'() {
        when:
        String testSession = null;  // use default
        String siteName = 'mike__reg'
        String testName = "11990"
        List<String> sections = new ArrayList<>()
        Map<String, String> params = new HashMap<>()
        params.put('$patientid$', patientId)
        boolean stopOnFirstError = true

        and: 'Run Register test'
        List<Result> results = api.runTest(testSession, siteName, testName, sections, params, stopOnFirstError)

        then:
        true
        results.size() == 1
        results.get(0).passed()
    }

    def 'Run test under debug'() {
        when:
        String testSession = null;  // use default
        String siteName = 'mike__reg'
        String testName = "12002"
        List<String> sections = new ArrayList<>()
        Map<String, String> params = new HashMap<>()
        params.put('$patientid$', patientId)
        boolean stopOnFirstError = true

        and: 'Run Register test'
        List<Result> results = api.runTest(testSession, siteName, testName, sections, params, stopOnFirstError)

        then:
        true
        results.size() == 1
        results.get(0).passed()
    }

    def 'Run all registry tests'() {
        when:
        String testSession = null;  // use default
        String siteName = 'mike__reg'
        String testName = "tc:R.b"
        List<String> sections = new ArrayList<>()
        Map<String, String> params = new HashMap<>()
        params.put('$patientid$', patientId)
        boolean stopOnFirstError = true

        and: 'Run'
        List<Result> results = api.runTest(testSession, siteName, testName, sections, params, stopOnFirstError)

        then:
        true
        results.size() == 1
        results.get(0).passed()
    }

}