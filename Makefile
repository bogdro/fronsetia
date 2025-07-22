# Fronsetia Makefile
#
# Copyright (C) 2011-2025 Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
#
# This file is part of Fronsetia (Free Online Service Testing Application),
#  a web application that allows testing webservices.
#
# Project homepage: https://fronsetia.sourceforge.io/
#
#  This program is free software: you can redistribute it and/or modify
#  it under the terms of the GNU Affero General Public License as published by
#  the Free Software Foundation, either version 3 of the License, or
#  (at your option) any later version.
#
#  This program is distributed in the hope that it will be useful,
#  but WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  GNU Affero General Public License for more details.
#
#  You should have received a copy of the GNU Affero General Public License
#  along with this program.  If not, see <http://www.gnu.org/licenses/>.
#

NAME = fronsetia

RMDIR = /bin/rm -fr
# when using '-p', no error is generated when the directory exists
MKDIR = /bin/mkdir -p
COPY = /bin/cp -pRf
CHMOD = /bin/chmod
GREP = /bin/grep
PERL = /usr/bin/perl

VER = $(shell $(GREP) FRONSETIA_VERSION pom.xml | $(PERL) -pe 's#\s*<version>([^<]+)</version>.*#$$1#')

# Use the GNU tar format
# ifneq ($(shell tar --version | grep -i bsd),)
# PACK1_GNUOPTS = --format gnutar
# endif
PACK = /bin/tar $(PACK1_GNUOPTS) -vzcf
PACK_EXT = tar.gz

#PACK2 = /usr/bin/gzip -9
#PACK2_EXT = .gz

MAVEN = mvn

SUBDIRS = src

EXTRA_DIST = AUTHORS ChangeLog COPYING INSTALL-*.txt Makefile \
	NEWS pom.xml README

FILE_ARCH_SRC = $(NAME)-$(VER)-src.$(PACK_EXT)

all:	package

dist:	$(FILE_ARCH_SRC)

$(FILE_ARCH_SRC): $(EXTRA_DIST) \
		$(shell find $(SUBDIRS) -type f)
	$(RMDIR) $(NAME)-$(VER)
	$(MKDIR) $(NAME)-$(VER)
	$(COPY) $(EXTRA_DIST) $(SUBDIRS) $(NAME)-$(VER)
	$(PACK) $(FILE_ARCH_SRC) $(NAME)-$(VER)
	$(RMDIR) $(NAME)-$(VER)

package: target/$(NAME)-web-$(VER).war

war: package

target/$(NAME)-web-$(VER).war: $(shell find src -type f) Makefile pom.xml
	$(MAVEN) clean package -DskipTests=true

clean:
	$(MAVEN) clean

test:
	$(MAVEN) clean test

reinit:
	$(MAVEN) archetype:generate -B -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-webapp -DgroupId=bogdrosoft.$(NAME) -DartifactId=$(NAME)-web -Dversion=1.0

.PHONY: all clean dist package reinit test war
