/*************************************************************************
 * Copyright 2009-2013 Eucalyptus Systems, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 * Please contact Eucalyptus Systems, Inc., 6755 Hollister Ave., Goleta
 * CA 93117, USA or visit http://www.eucalyptus.com/licenses/ if you need
 * additional information or have any questions.
 ************************************************************************/
package com.eucalyptus.cloudformation.entity;

import com.eucalyptus.cloudformation.StackResource;
import com.eucalyptus.entities.Entities;
import com.eucalyptus.entities.TransactionResource;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by ethomas on 12/19/13.
 */
public class StackResourceEntityManager {
  public static void addStackResource(StackResource stackResource, JsonNode metadata, String accountId) {
    try ( TransactionResource db =
            Entities.transactionFor( StackResourceEntity.class ) ) {
      Entities.persist(stackResourceToStackResourceEntity(stackResource, metadata, accountId));
      db.commit( );
    }

  }

  public static void updatePhysicalResourceId(String stackId, String logicalResourceId, String physicalResourceId, String accountId) {
    try ( TransactionResource db =
            Entities.transactionFor( StackResourceEntity.class ) ) {
      Criteria criteria = Entities.createCriteria(StackResourceEntity.class)
        .add(Restrictions.eq("accountId" , accountId))
        .add(Restrictions.eq("stackName", stackId))
        .add(Restrictions.eq("logicalResourceId" , logicalResourceId))
        .add(Restrictions.eq("recordDeleted", Boolean.FALSE));
      List<StackResourceEntity> entityList = criteria.list();
      for (StackResourceEntity stackResourceEntity: entityList) {
        stackResourceEntity.setPhysicalResourceId(physicalResourceId);
      }
      db.commit( );
    }

  }

  public static void updateStatus(String stackId, String logicalResourceId, StackResourceEntity.Status status, String statusReason, String accountId) {
    try ( TransactionResource db =
            Entities.transactionFor( StackResourceEntity.class ) ) {
      Criteria criteria = Entities.createCriteria(StackResourceEntity.class)
        .add(Restrictions.eq("accountId" , accountId))
        .add(Restrictions.eq("stackId", stackId))
        .add(Restrictions.eq("logicalResourceId", logicalResourceId))
        .add(Restrictions.eq("recordDeleted", Boolean.FALSE));
      List<StackResourceEntity> entityList = criteria.list();
      for (StackResourceEntity stackResourceEntity: entityList) {
        stackResourceEntity.setResourceStatus(status);
        stackResourceEntity.setResourceStatusReason(statusReason);
      }
      db.commit( );
    }
  }

  public static StackResourceEntity getStackResource(String stackId, String logicalResourceId, String accountId) {
    StackResourceEntity stackResourceEntity = null;
    try ( TransactionResource db =
            Entities.transactionFor( StackResourceEntity.class ) ) {
      Criteria criteria = Entities.createCriteria(StackResourceEntity.class)
        .add(Restrictions.eq("accountId" , accountId))
        .add(Restrictions.eq("stackId" , stackId))
        .add(Restrictions.eq("logicalResourceId" , logicalResourceId))
        .add(Restrictions.eq("recordDeleted", Boolean.FALSE));
      List<StackResourceEntity> stackResourceEntityList = criteria.list();
      if (stackResourceEntityList != null && !stackResourceEntityList.isEmpty()) {
        stackResourceEntity = stackResourceEntityList.get(0);
      }
      db.commit( );
    }
    return stackResourceEntity;
  }

  public static List<StackResourceEntity> getStackResources(String stackId, String accountId) {
    List<StackResourceEntity> stackResourceEntityList = Lists.newArrayList();
    try ( TransactionResource db =
            Entities.transactionFor( StackResourceEntity.class ) ) {
      Criteria criteria = Entities.createCriteria(StackResourceEntity.class)
        .add(Restrictions.eq( "accountId" , accountId))
        .add(Restrictions.eq( "stackId" , stackId))
        .add(Restrictions.eq("recordDeleted", Boolean.FALSE));
      stackResourceEntityList = criteria.list();
      db.commit( );
    }
    return stackResourceEntityList;
  }

  public static void deleteStackResources(String stackId, String accountId) {
    try ( TransactionResource db =
            Entities.transactionFor( StackResourceEntity.class ) ) {
      Criteria criteria = Entities.createCriteria(StackResourceEntity.class)
        .add(Restrictions.eq( "accountId" , accountId))
        .add(Restrictions.eq( "stackName" , stackId))
        .add(Restrictions.eq("recordDeleted", Boolean.FALSE));
      for (StackResourceEntity stackResourceEntity: (List<StackResourceEntity>) criteria.list()) {
        stackResourceEntity.setRecordDeleted(Boolean.TRUE);
      }
      db.commit( );
    }
  }

  public static StackResource stackResourceEntityToStackResource(StackResourceEntity stackResourceEntity) {
    StackResource stackResource = new StackResource();
    stackResource.setDescription(stackResourceEntity.getDescription());
    stackResource.setLogicalResourceId(stackResourceEntity.getLogicalResourceId());
    stackResource.setPhysicalResourceId(stackResourceEntity.getPhysicalResourceId());
    stackResource.setResourceStatus(stackResourceEntity.getResourceStatus().toString());
    stackResource.setResourceStatusReason(stackResourceEntity.getResourceStatusReason());
    stackResource.setResourceType(stackResourceEntity.getResourceType());
    stackResource.setStackId(stackResourceEntity.getStackId());
    stackResource.setStackName(stackResourceEntity.getStackName());
    stackResource.setTimestamp(stackResourceEntity.getLastUpdateTimestamp());
    return stackResource;
  }

  public static StackResourceEntity stackResourceToStackResourceEntity(StackResource stackResource, JsonNode metadataJsonNode, String accountId) {
    StackResourceEntity stackResourceEntity = new StackResourceEntity();
    stackResourceEntity.setRecordDeleted(Boolean.FALSE);
    stackResourceEntity.setAccountId(accountId);
    stackResourceEntity.setDescription(stackResource.getDescription());
    stackResourceEntity.setLogicalResourceId(stackResource.getLogicalResourceId());
    stackResourceEntity.setPhysicalResourceId(stackResource.getPhysicalResourceId());
    stackResourceEntity.setResourceStatus(StackResourceEntity.Status.valueOf(stackResource.getResourceStatus()));
    stackResourceEntity.setResourceStatusReason(stackResource.getResourceStatusReason());
    stackResourceEntity.setResourceType(stackResource.getResourceType());
    stackResourceEntity.setStackId(stackResource.getStackId());
    stackResourceEntity.setStackName(stackResource.getStackName());
    stackResourceEntity.setMetadata(metadataJsonNode != null ? metadataJsonNode.toString() : null);
    return stackResourceEntity;
  }
}
