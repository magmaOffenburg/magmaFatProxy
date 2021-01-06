1.2.0 (January 6, 2021)
------------------------------
* Fixed most of the occurring self collisions
* The horizontal kick angle is now relative to the agent
* The 2D projected torso position will now be used for calculating the ball distance in the kick
* magmaFatProxy now prevents kick-ins caused by pulling the ball out of the field by a previous kick-in. This is accomplished by setting the playmode to `PLAY_ON` while executing a kick-in.
* Updated RSG Parser to understand pass mode nodes

1.1.0 (September 28, 2020)
------------------------------
* Fixed a `NoSuchMethodError` in the generated JAR file.
* *Internal: Switched from Ant to Maven*
* *Internal: Updated libraries*

1.0.1 (September 15, 2020)
------------------------------
* Lazy creation of monitor connection (see #1)
* Fixed misplaced shebang in start script

1.0.0 (May 29, 2020)
------------------------------
*Initial release*
