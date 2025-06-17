//package com.tasktk;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.ws.rs.GET;
//import jakarta.ws.rs.Path;
//
//@Path("/test")
//public class TestResource {
//    @PersistenceContext(unitName = "TasktkPu")
//    private EntityManager em;
//
//    @GET
//    public String test() {
//        TestEntity entity = em.find(TestEntity.class, 1L);
//        return entity != null ? entity.getName() : "Not found but db connected  .............";
//    }
//}