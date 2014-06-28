IPT-Mobile-Presentation
=======================

IPT Mobile Presentation demonstrates interactive mobile presentation and login event notifications using WebSocket, JAX-RS (REST) & jQuery Mobile

Copyright (c) 2012 - 2014 IPT - Intellectual Products & Technologies Ltd. All rights reserved.
E-mail: office@iproduct.org,
Web: http://iproduct.org/

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License (the "License")
as published by the Free Software Foundation version 2 of the License.
You may not use this file except in compliance with the License. You can
obtain a copy of the License at root directory of this project in file
LICENSE.txt.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.

When distributing the software, include this COPYRIGHT & LICENSE HEADER
in each file, and include the License file LICENSE.txt in the root directory
of your distributable.

The folder IPT-Mobile-Presentation contains a NetBeans web project that could be
deployed on Java EE 7 Appliction Server (e.g. Glassfish 4)

Before deploing the demo application you should:

1. Create a DB schema "ipt_presentation" by running the MySQL DB script sql/create.sql

2. Create a DataSource for the created DB schema (ipt_presentation) and map it to the JNDI name "jdbc/ipt_presentation" referred in persistence.xml configuration file.

