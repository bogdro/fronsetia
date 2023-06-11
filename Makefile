# ========== you can change:

LIB_PATH	= /home/bogdan/RPMS/java/apache-tomcat/lib

CLASSES		= $(LIB_PATH)/wsdl4j.jar
CLASSES		:= $(CLASSES):$(LIB_PATH)/xbean.jar
CLASSES		:= $(CLASSES):$(LIB_PATH)/httpclient-4.1.1.jar
CLASSES		:= $(CLASSES):$(LIB_PATH)/httpcore-4.1.jar
CLASSES		:= $(CLASSES):$(LIB_PATH)/commons-logging-1.1.1.jar
CLASSES		:= $(CLASSES):$(LIB_PATH)/servlet-api.jar
CLASSES		:= $(CLASSES):$(LIB_PATH)/axis2-saaj-1.6.1.jar
CLASSES		:= $(CLASSES):$(LIB_PATH)/axiom.jar

# ========== you can change (only if necessary):

JAVAC		= javac
JAVAC_OPTS	= -Xlint -cp .:$(CLASSES)
JAVA		= java
JAVA_OPTS	= -cp .:$(CLASSES)
CP		= /bin/cp -f
DEL		= rm -fr
DIST_PACKER	= tar zcf
DIST_EXT	= tar.gz
ZIP_PACKER	= zip -9 -r
HASH_ALGO	= RIPEMD160

# ========== don't change ====================================================

PROG_NAME	= SOAPServiceTester
PROG_VERSION	= 0.3

PACKAGE_DIR	= BogDroSoft/soaptest

JAVA_FILES	= $(PACKAGE_DIR)/WSDLCheck.java $(PACKAGE_DIR)/RequestUtilities.java \
	$(PACKAGE_DIR)/OperationLauncher.java $(PACKAGE_DIR)/SOAPInterpreter.java

comma		= ,
empty		=
space		= $(empty) $(empty)

TARGETS		:= $(patsubst %.java,%.class,$(JAVA_FILES))


all: compile

compile: $(TARGETS) Makefile

%.class: %.java Makefile
	$(JAVAC) $(JAVAC_OPTS) $<
#$(CP) $@ deploy/WEB-INF/classes/$(PACKAGE_DIR)

deploy:	compile Makefile
	$(CP) $(PACKAGE_DIR)/*.class deploy/WEB-INF/classes/$(PACKAGE_DIR)

war:	$(PROG_NAME).war

$(PROG_NAME).war: deploy Makefile $(shell find deploy)
	$(DEL) $(PROG_NAME).war
	cd deploy && $(ZIP_PACKER) ../$(PROG_NAME).war .

dist:	$(PROG_NAME)-$(PROG_VERSION).$(DIST_EXT)

$(PROG_NAME)-$(PROG_VERSION).$(DIST_EXT): Makefile AUTHORS COPYING ChangeLog Makefile INSTALL \
		README $(JAVA_FILES)
	$(DEL) deploy/WEB-INF/classes/$(PACKAGE_DIR)/*.class && \
	cd .. && \
	$(DEL) $(PROG_NAME)-$(PROG_VERSION).$(DIST_EXT) $(PROG_NAME)-$(PROG_VERSION).$(DIST_EXT).asc \
		$(PROG_NAME)-$(PROG_VERSION) && \
	cp -r $(PROG_NAME) $(PROG_NAME)-$(PROG_VERSION) && \
	tar zcf $(PROG_NAME)-$(PROG_VERSION).$(DIST_EXT) \
		$(PROG_NAME)-$(PROG_VERSION)/AUTHORS	\
		$(PROG_NAME)-$(PROG_VERSION)/COPYING	\
		$(PROG_NAME)-$(PROG_VERSION)/ChangeLog	\
		$(PROG_NAME)-$(PROG_VERSION)/Makefile	\
		$(PROG_NAME)-$(PROG_VERSION)/INSTALL	\
		$(PROG_NAME)-$(PROG_VERSION)/README	\
		$(PROG_NAME)-$(PROG_VERSION)/deploy	\
		$(PROG_NAME)-$(PROG_VERSION)/{$(subst $(space),$(comma),$(JAVA_FILES))}	&& \
	$(DEL) $(PROG_NAME)-$(PROG_VERSION) && \
	gpg --digest-algo $(HASH_ALGO) -ba $(PROG_NAME)-$(PROG_VERSION).$(DIST_EXT)
#$(DIST_PACKER) $(PROG_NAME)-$(PROG_VERSION).$(DIST_EXT) AUTHORS \
#	$(JAVA_FILES) ChangeLog COPYING deploy INSTALL Makefile README

