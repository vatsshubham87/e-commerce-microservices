//package org.microservicesrevision.user.seeders;
//
//import org.microservicesrevision.user.model.Role;
//import org.microservicesrevision.user.repo.RoleRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.Arrays;
//
//@Component
//public class RoleSeeder implements CommandLineRunner {
//
//    private final RoleRepository roleRepository;
//
//    public RoleSeeder(RoleRepository roleRepository) {
//        this.roleRepository = roleRepository;
//    }
//
//    @Override
//    public void run(String... args) {
//        if (roleRepository.count() == 0) {
//            Role admin = new Role();
//            admin.setRoleName("ADMIN");
//
//            Role customer = new Role();
//            customer.setRoleName("CUSTOMER");
//
//            Role seller = new Role();
//            seller.setRoleName("SELLER");
//
//            roleRepository.saveAll(Arrays.asList(admin, customer, seller));
//            System.out.println("✅ Roles seeded successfully!");
//        } else {
//            System.out.println("ℹ️ Roles already exist, skipping seeding...");
//        }
//    }
//}
