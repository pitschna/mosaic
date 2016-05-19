# mosaic

* Read in the original File
* Find out the size of the tiles by dividing the orignial file by the selected number of horizontal tiles

              +--------------------------+              +------+------+------+------+
              | Original File            |              | o    | o    | o    | o    |
              |                          |              |      |      |      |      |
              |                          |              |      |      |      |      |
              |                          |  +-------->  +---------------------------+
              |                          |              | o    | o    | o    | o    |
              |                          |              |      |      |      |      |
              |                          |              |      |      |      |      |
              |                          |              +------+------+------+------+
              +--------------------------+



* Generate the tiles by miniturazing a bunch of images or parts of them to the size of the tiles


               +------------+
             +-----------+  |               +-----------+
             |           |  |               |           |
           +-+--------+  |  |               |           |
           |          |  |  |               |           |                 +-------+   +-------+  +-------+  +-------+
           |          |  |  |   +------->   |           |   +-------->    | t     |   | t     |  | t     |  | t     |
           |          |  |  |               |           |                 |       |   |       |  |       |  |       |
           |          |  |  |               |           |                 |       |   |       |  |       |  |       |
           |          |  +--+               |           |                 +-------+   +-------+  +-------+  +-------+
           |          |  |                  +-----------+
           |          +--+
           +----------+



 * Pixel by pixel MSD

                   +-------+-------+   +-------+-------+                                +------+------+------+------+
                   |o1c    | o2c   |   |t1c    | t2c   |                                | t    | t    | t    | t    |
                   |       |       |   |       |       |                                |      |      |      |      |
                   |       |       |   |       |       |                                |      |      |      |      |
                   +---------------+   +---------------+   +------->   MSD  +------->   +---------------------------+
                   |o3c    | o4c   |   |t3c    | t4c   |                                | t    | t    | t    | t    |
                   |       |       |   |       |       |                                |      |      |      |      |
                   |       |       |   |       |       |                                |      |      |      |      |
                   +-------+-------+   +-------+-------+                                +------+------+------+------+

* find best match
* stitch together and correct color to match average of original tile
