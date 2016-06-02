# a-to-b-via-satellite
Have you ever wondered how to calculate best route from A to B via satellites if you know their lat and long? Well the guys at Reaktor had and placed the OrbitalChallenge. Here's my take on it.

The assignment was that we have latitude and longitude for starting point and ending point. Additionally we have a list of satellites for which we know the latitude, longitude and altitude. The earth is considered as perfectly round with radius of 6371 km. Each jump to be valid between A to B needs to have a line of sight. What's the optimal route between A and B?

I didn't do research on the subject and wanted to pull it out of my own head. So no any fancy path finding algorigthms here. The only thing I had to peek was how to calculate the angle between two vectors in 3D space.

