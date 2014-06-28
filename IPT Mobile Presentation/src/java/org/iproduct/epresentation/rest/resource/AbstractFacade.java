/* COPYRIGHT & LICENSE HEADER
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * IPT Mobile Presentation demonstrates interactive mobile presentation
 * and login event notifications using WebSocket, JAX-RS (REST) & jQuery Mobile
 *
 * Copyright (c) 2012 - 2014 IPT - Intellectual Products & Technologies Ltd. 
 * All rights reserved.
 * 
 * E-mail: office@iproduct.org
 * Web: http://iproduct.org/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License (the "License") 
 * as published by the Free Software Foundation version 2 of the License.
 * You may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at root directory of this project in file 
 * LICENSE.txt.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * When distributing the software, include this COPYRIGHT & LICENSE HEADER  
 * in each file, and include the License file LICENSE.txt in the root directory
 * of your distributable.
 *
 * GPL Classpath Exception:
 * IPT - Intellectual Products & Technologies (IPT) designates this particular 
 * file as subject to the "Classpath" exception as provided by IPT in 
 * the GPL Version 2 License file that accompanies this code.
 * 
 * In case you modify this file,
 * please add the appropriate notice below the existing Copyright notices, 
 * with the fields enclosed in brackets {} replaced by your own identification:
 * "Portions Copyright (c) {year} {name of copyright owner}"
 */

package org.iproduct.epresentation.rest.resource;

import java.util.List;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * This class represents abstract facade implementing common db persistence 
 * operations to be inherited by concrete REST resource classes
 *
 * @author Trayan Iliev
 */
public abstract class AbstractFacade<T> {
    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        try {
            getEntityManager().persist(entity);
        } catch (ConstraintViolationException ex) {
            StringBuilder error = new StringBuilder("<ul>");
            for (ConstraintViolation c : ex.getConstraintViolations()) {
                error.append("<li>property '").
                    append(c.getPropertyPath()).append("' has invalid value: '")
                    .append(c.getInvalidValue()).append("' - ")
                    .append(c.getMessage()).append("</li>");
            }
            error.append("</ul>");
            throw new WebApplicationException(
                    Response.serverError().type(MediaType.TEXT_HTML)
                    .entity(error.toString()).build());
        }
    }

    public void edit(T entity) {
        try {
            EntityManager em = getEntityManager();
            em.merge(entity);
            em.flush();
            //utx.commit();
        } catch (ConstraintViolationException ex) {
            StringBuilder error = new StringBuilder("<ul>");
            for (ConstraintViolation c : ex.getConstraintViolations()) {
                error.append("<li>property '").
                    append(c.getPropertyPath()).append("' has invalid value: '")
                    .append(c.getInvalidValue()).append("' - ")
                    .append(c.getMessage()).append("</li>");
            }
            error.append("</ul>");
            
            System.err.println(error.toString());
            
            throw new WebApplicationException(
                Response.serverError().type(MediaType.TEXT_HTML)
                .entity(error.toString()).build());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException (e);
        }
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
}
