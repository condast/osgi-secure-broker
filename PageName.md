# Introduction #

A Secure Broker for OSGI Declarative Services


# Details #

This project provides a Secure Broker for OSGI declarative services. I often want to secure my declarative services, so that these services are not accessible for any bundle, and while I was working on this, I realised that there were some other issues I was having with DS that I decided to tackle as well. The result is a secure broker that mediates between servers and clients, which has a footprint of give or take 30 kBytes, and has greatly improved the development time of DS in my projects. For a full discussion, see http://java.dzone.com/articles/secure-broker-osgi for details.

The secure broker can be added to any OSGI container by including the org.eclipselabs.osgi.broker.ds plugin to the bundle repository. In order for it to work, you need to do the following:

1: create a new plugin project
2: copy the component.xml described in the DZone article to the OSGI-INF directory of your project and register it in the Manifest.MF file
3: copy the RoundhouseComponent to your source file.
4: rename the component is and the component class in component.xml to your project
5: Create a run configuration that activates your project and org.eclipselabs.osgi.broker.ds
6: Check if everything works by typing 'roundhouse' in the OSGI console. This will provide a list of all the commands that are supported.

You can, of course, also start with the sample plugins and modify them to your needs.

KNOWN ISSUES:

Currently there is one known potential weakness in the secure broker, which is that a rogue bundle that is started prior to the other bundles, may 'capture' an assembly by offering easy access and setting 'claimAttention' true. This will be fixed in a future version.
Another issues is that it should be possible to hide broker services from the OSGI console upon deployment.

Both issues are not real showstoppers though, as they can be easily fixed