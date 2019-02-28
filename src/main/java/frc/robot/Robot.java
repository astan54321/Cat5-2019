package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.controls.Controls;
import frc.robot.controls.DriverStation;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.ShiftingWestCoast;
import frc.robot.subsystems.Arm.Mode;
import frc.robot.subsystems.Arm.Position;
import frc.robot.subsystems.ShiftingWestCoast.DriveMode;

public class Robot extends TimedRobot {

  ShiftingWestCoast drive;
  DriverStation ds;
  Arm arm;

  @Override
  public void robotInit() {
    drive = new ShiftingWestCoast();
    ds = new DriverStation();
    arm = new Arm();
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopPeriodic() {
    teleopControl();
  }

  @Override
  public void testPeriodic() {
  }

  public void teleopControl() {
    driveTrainControl();
     cargoControl(Mode.MAGIC); // can change this so it can be changes mid-match
    // TODO: add hatch control

  }

  public void driveTrainControl() {
    double speedInput = ds.getGTASpeed();
    double turnInput = ds.getTurn();
    boolean highGear = ds.getHighGear();
    drive.drive(DriveMode.kCurve, speedInput, turnInput, Controls.SENSITIVITY);
    drive.shift(highGear);
  }

  public void cargoControl(Mode m) {
    if (m == Mode.MAGIC)

    { // Motion Magic
      Position p = Position.STOWED; // Default state
      boolean waitForShoot = false;
      double intakeSpeed = Constants.CARGO_HOLDING_SPEED; // TODO: set intake to brake mode so it holds the ball without
                                                          // a constant infeed

      if (ds.getCargoGathering()) {
        p = Position.PICKUP;
        intakeSpeed = Constants.CARGO_INTAKE_SPEED;
        waitForShoot = false;
        // TODO set intake wheels to intake here
      } else if (ds.getCargoRocketLevel()) {
        p = Position.ROCKET;
        intakeSpeed = Constants.CARGO_SHOOT_SPEED;
        waitForShoot = true;
      } else if (ds.getCargoShipLevel()) {
        p = Position.CARGO_SHIP;
        intakeSpeed = Constants.CARGO_SHOOT_SPEED;
        waitForShoot = true;
      }

      arm.operate(p);
      if (waitForShoot) {
        if (ds.shootCargo()) {
          // set intake to intakeSpeed if DriverStation.shootCargo()
        }
      } else {
        // just set intake to intakeSpeed
      }
    } else if (m == Mode.MANUAL) {
      arm.manualOperate(ds.getArmControl() * 0.8);
      // implement something for manual control ONLY while tuning the PIDF values. DO
      // NOT
      // use manual control in a match unless something breaks. USE MOTON MAGIC
    }
  }

  // public void hatchControl() {
  // TODO add hatch class and control for it here
  // Recommend:
  // hold button = Hatch extend out, claw close
  // release button = hatch retract, claw open
}