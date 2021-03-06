package gov.nist.toolkit.xdstools2.server.api

import gov.nist.toolkit.services.server.SimulatorApi
import gov.nist.toolkit.services.server.UnitTestEnvironmentManager
import gov.nist.toolkit.session.server.Session
import gov.nist.toolkit.simcommon.client.Simulator
import spock.lang.Specification

/**
 * Created by bill on 6/15/15.
 */
class SimulatorApiITh extends Specification {
    Session session
    String simId = 'myreg'

    def setup() {
        session = UnitTestEnvironmentManager.setupLocalToolkit()
    }

    def 'Create, test, delete Simulator'() {
        when:
        SimulatorApi simApi = new SimulatorApi(session)
        Simulator sim = simApi.create('reg', simId)
        println sim.toString()

        then:
        simApi.exists(simId)

        when:
        simApi.delete(simId)

        then:
        !simApi.exists(simId)
    }
}
