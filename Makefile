# ========== you can change:

LIB_PATH	= /home/bogdan/RPMS/java/apache-tomcat/lib

CLASSES		= $(LIB_PATH)/wsdl4j.jar
CLASSES		:= $(CLASSES):$(LIB_PATH)/xbean.jar
CLASSES		:= $(CLASSES):$(LIB_PATH)/httpclient-4.1.1.jar
CLASSES		:= $(CLASSES):$(LIB_PATH)/httpcore-4.1.jar
CLASSES		:= $(CLASSES):$(LIB_PATH)/commons-logging-1.1.1.jar

# ========== you can change (only if necessary):

JAVAC		= javac
JAVAC_OPTS	= -Xlint -cp .:$(CLASSES)
JAVA		= java
JAVA_OPTS	= -cp .:$(CLASSES)
CP		= /bin/cp -f
DEL		= rm -f
DIST_PACKER	= tar zcf
ZIP_PACKER	= zip -9 -r

# ========== don't change ====================================================

PROG_NAME	= SOAPServiceTester
PROG_VERSION	= 0.1

PACKAGE_DIR	= BogDroSoft/soaptest

JAVA_FILES	= $(PACKAGE_DIR)/WSDLCheck.java

TARGETS		:= $(patsubst %.java,%.class,$(JAVA_FILES))

all: compile

compile: $(TARGETS)

%.class: %.java
	$(JAVAC) $(JAVAC_OPTS) $<

deploy:	compile Makefile
	$(CP) $(PACKAGE_DIR)/*.class deploy/WEB-INF/classes/$(PACKAGE_DIR)

war:	deploy Makefile
	$(DEL) $(PROG_NAME).war
	cd deploy && $(ZIP_PACKER) ../$(PROG_NAME).war .

dist:	Makefile
	$(DEL) deploy/WEB-INF/classes/$(PACKAGE_DIR)/*.class
	$(DIST_PACKER) $(PROG_NAME)-$(PROG_VERSION).tar.gz AUTHORS \
		$(JAVA_FILES) ChangeLog COPYING deploy INSTALL Makefile README

