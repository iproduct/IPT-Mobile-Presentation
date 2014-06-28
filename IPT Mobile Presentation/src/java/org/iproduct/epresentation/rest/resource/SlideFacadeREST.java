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

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.ws.rs.*;
import org.iproduct.epresentation.entity.Presentation;
import org.iproduct.epresentation.entity.Slide;
import org.iproduct.epresentation.entity.Slide_;

/**
 * This class represents slides resource and allows management of 
 * different slides using REST methods
 *
 * @author Trayan Iliev
 */
@Stateless
@Path("{presentationId}/slide")
public class SlideFacadeREST extends AbstractFacade<Slide> {
    @PersistenceContext(unitName = "ePresentationPU")
    private EntityManager em;

    public SlideFacadeREST() {
        super(Slide.class);
    }

    @POST
    @Consumes({"application/xml", "application/json"})
    public void create(@PathParam("presentationId") Integer presId, Slide entity) {
        Date now = new Date();
        entity.setCreated(now);
        entity.setModified(now);
        entity.setId(null);
        Presentation p = getEntityManager().find(Presentation.class, presId);
        entity.setPresentationId(p);
        p.getSlideList().add(entity);
        super.create(entity);
    }

    @PUT
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("presentationId") Integer presId, Slide entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Slide find(@PathParam("presentationId") Integer presId, @PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<Slide> findAll(@PathParam("presentationId") Integer presId) {
        return findAllSlides(presId);
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Slide> findRange(@PathParam("presentationId") Integer presId, 
        @PathParam("from") Integer from, @PathParam("to") Integer to) {
        return findRange(presId, new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST(@PathParam("presentationId") Integer presId) {
        return String.valueOf(count(presId));
    }

    @java.lang.Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    //private methods
    private List<Slide> findAllSlides(Integer presentationId) {
        Presentation presentation = getEntityManager().find(Presentation.class, presentationId);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Slide> cq = cb.createQuery(Slide.class);
//        Metamodel m = em.getMetamodel();
//        EntityType<Slide> Slide_ = m.entity(Slide.class);
        Root<Slide> slide = cq.from(Slide.class);
        cq.select(slide)
           .where(cb.equal(slide.get("presentationId"), presentation));
        return em.createQuery(cq).getResultList();
    }

    private List<Slide> findRange(Integer presentationId, int[] range) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Slide> cq = cb.createQuery(Slide.class);
        Metamodel m = em.getMetamodel();
        EntityType<Slide> Slide_ = m.entity(Slide.class);
        Root<Slide> slide = cq.from(Slide.class);
        cq.select(slide)
           .where(cb.equal(slide.get("presentationId"), presentationId));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    private int count(Integer presentationId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Slide> slide = cq.from(Slide.class);
        cq.select(cb.count(slide))
           .where(cb.equal(slide.get(Slide_.presentationId), presentationId));
        Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
