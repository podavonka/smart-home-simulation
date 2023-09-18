**Autoři:** Valeriia Klimova, Tigran Oganesian

The project is implemented as a semestral work for the subject B6B36OMO. It consists in creating a simulation of life in a smart home and includes the use of design patterns, making class and use case diagrams.

## 1. Our achievments
- [X] F01. _Various entities are implemeted in the project._
- [X] F02. _Devices are controlled by API that can switch their states._
- [X] F03. _Devices consume a different amount of electricity in each state._
- [X] F04. _Devices provide information about their consumption and functinality._
- [X] F05. _Creatures can interact with home items and perform actions related to them._
- [X] F06. _Events are generated autonomously each iteration with a certain probability._
- [X] F07. _Events are handled by creatures and devices._
- [X] F08. _After one virtual day, reports about activity and usage, consumption and events are generated._
- [X] F09. _If device breaks, resident reads a manual and makes repairs._
- [X] F10. _Residents use both devices and sport equipment._

## 2. Patterns used
- [X] State machine: _Device, DeviceContext, DeviceState, ActiveStatem, TurnedOffState, IdleState_
- [X] Iterator: _HouseController, SimulationIterator, CustomIterator_
- [X] Factory/Factory method: _Creature, Person, Pet_
- [ ] Decorator/Composite
- [X] Singleton: HouseController
- [X] Visitor/Observer/Listener: _HouseController(Observable), Creature(Observer)_
- [X] Chain of responsibility: _FixAction, FixActionStep, ReadManualStep, FixStep_
- [ ] Partially persistent data structure
- [ ] Object Pool
- [X] Lazy Initialization: _HouseController_


