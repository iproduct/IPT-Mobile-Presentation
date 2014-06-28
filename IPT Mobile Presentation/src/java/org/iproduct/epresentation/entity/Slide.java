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

package org.iproduct.epresentation.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * This entity class models a single presentation slide
 * 
 * @author Trayan Iliev
 */
@Entity
@Table(name = "slide")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Slide.findAll", query = "SELECT s FROM Slide s"),
    @NamedQuery(name = "Slide.findById", query = "SELECT s FROM Slide s WHERE s.id = :id"),
    @NamedQuery(name = "Slide.findByCaption", query = "SELECT s FROM Slide s WHERE s.caption = :caption"),
    @NamedQuery(name = "Slide.findByAudience", query = "SELECT s FROM Slide s WHERE s.audience = :audience"),
    @NamedQuery(name = "Slide.findByDuration", query = "SELECT s FROM Slide s WHERE s.duration = :duration"),
    @NamedQuery(name = "Slide.findByCreated", query = "SELECT s FROM Slide s WHERE s.created = :created"),
    @NamedQuery(name = "Slide.findByModified", query = "SELECT s FROM Slide s WHERE s.modified = :modified"),
    @NamedQuery(name = "Slide.findByMimetype", query = "SELECT s FROM Slide s WHERE s.mimetype = :mimetype")})
public class Slide implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @TableGenerator(name="SLIDE_TABLE_GEN", table="SEQUENCE_TABLE", pkColumnName="SEQ_NAME",
        valueColumnName="SEQ_COUNT", allocationSize=1, pkColumnValue="SLIDE_SEQ")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="SLIDE_TABLE_GEN")
    @Basic(optional = false)
    @NotNull
    @Column(name = "id", nullable = false)
    private Integer id;
    @Size(max = 255)
    @Column(name = "caption", length = 255)
    private String caption;
    @Size(max = 45)
    @Column(name = "author", length = 45)
    private String author;
    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;
    @Lob
    @Column(name = "picture")
    private byte[] picture;
    @Basic(optional = false)
    @NotNull
    @Column(name = "audience", nullable = false)
    private int audience;
    @Column(name = "duration")
    @Temporal(TemporalType.TIME)
    private Date duration;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Basic(optional = false)
    @NotNull
    @Column(name = "modified", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modified;
    @Size(max = 45)
    @Column(name = "mimetype", length = 45)
    private String mimetype;
    @Size(max = 255)
    @Column(name = "data_url", length = 255)
    private String dataUrl;
    @JoinColumn(name = "presentation_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Presentation presentationId;
    @JoinColumn(name = "previous", referencedColumnName = "id")
    @OneToOne
    private Slide previous;
    @JoinColumn(name = "next", referencedColumnName = "id")
    @OneToOne
    private Slide next;

    public Slide() {
    }

    public Slide(Integer id) {
        this.id = id;
    }

    public Slide(Integer id, int audience, Date created, Date modified) {
        this.id = id;
        this.audience = audience;
        this.created = created;
        this.modified = modified;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public int getAudience() {
        return audience;
    }

    public void setAudience(int audience) {
        this.audience = audience;
    }

    public Date getDuration() {
        return duration;
    }

    public void setDuration(Date duration) {
        this.duration = duration;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    @XmlTransient
    public Presentation getPresentationId() {
        return presentationId;
    }

    public void setPresentationId(Presentation presentationId) {
        this.presentationId = presentationId;
    }
    
    @XmlElement(name="presentationId")
    public Integer getPresentationIdNumber() {
        return presentationId.getId();
    }

    public Slide getPrevious() {
        return previous;
    }

    public void setPrevious(Slide previous) {
        this.previous = previous;
    }

    public Slide getNext() {
        return next;
    }

    public void setNext(Slide next) {
        this.next = next;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Slide)) {
            return false;
        }
        Slide other = (Slide) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.iproduct.epresentation.entity.Slide[ id=" + id + " ]";
    }
    
}
