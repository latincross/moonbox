package moonbox.grid.deploy

import java.util.Date

import akka.actor.{ActorRef, Address}
import moonbox.grid.deploy.master.ApplicationType
import moonbox.grid.deploy.master.DriverState.DriverState


sealed trait DeployMessages extends Serializable

object DeployMessages {

	case object ElectedLeader extends DeployMessages

	case object RevokedLeadership extends DeployMessages

	case class BeginRecovery() extends DeployMessages

	case object CompleteRecovery extends DeployMessages

	case object CheckForWorkerTimeOut extends DeployMessages

	case class RegisterWorker(
		id: String,
		host: String,
		port: Int,
		worker: ActorRef,
		address: Address) extends DeployMessages {
	}

	case class MasterChanged(masterRef: ActorRef) extends DeployMessages

	case class WorkerStateResponse(id: String, drivers: Seq[(String, DriverDescription, Date)])

	case class WorkerLatestState(id: String,  drivers: Seq[(String, DriverDescription, Date)]) extends DeployMessages

	case class Heartbeat(workerId: String, worker: ActorRef) extends DeployMessages

	case class ReconnectWorker(masterRef: ActorRef) extends DeployMessages

	sealed trait RegisterWorkerResponse

	case class RegisteredWorker(masterAddress: ActorRef) extends DeployMessages with RegisterWorkerResponse

	case object MasterInStandby extends DeployMessages with RegisterWorkerResponse with RegisterApplicationResponse

	case class RegisterWorkerFailed(message: String) extends DeployMessages with RegisterWorkerResponse

	case object SendHeartbeat extends DeployMessages

	case class LaunchDriver(driverId: String, desc: DriverDescription) extends DeployMessages

	case class DriverStateChanged(
		driverId: String,
		state: DriverState,
		appId: Option[String],
		exception: Option[Exception])
	extends DeployMessages

	case class KillDriver(driverId: String) extends DeployMessages

	case class RegisterApplication(
		id: String,
		host: String,
		port: Int,
		endpoint: ActorRef,
		address: Address,
		dataPort: Int,
		appType: ApplicationType
	)

	sealed trait RegisterApplicationResponse

	case class RegisteredApplication(masterRef: ActorRef) extends RegisterApplicationResponse

	case class RegisterApplicationFailed(message: String) extends RegisterApplicationResponse

	case class ApplicationStateResponse(driverId: String) extends DeployMessages

}
