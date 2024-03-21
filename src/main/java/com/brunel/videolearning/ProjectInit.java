package com.brunel.videolearning;

import com.brunel.videolearning.model.VisitHistory;
import com.brunel.videolearning.repository.*;

import java.util.List;

import com.brunel.videolearning.model.FavoritesCourse;
import com.brunel.videolearning.model.User;
import com.brunel.videolearning.model.VideoCourse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@PropertySource("classpath:application.properties")
public class ProjectInit implements CommandLineRunner {

    Logger logger = LoggerFactory.getLogger(ProjectInit.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VideoCourseRepository videoCourseRepository;

    @Autowired
    private FavoritesCourseRepository favoritesCourseRepository;

    @Autowired
    private VisitHistoryRepository visitHistoryRepository;

    @Autowired
    private ChangingRoleTypeRepository changingRoleTypeRepository;

    @Autowired
    private EmailChangingRepository emailChangingRepository;


    @Override
    public void run(String... args) throws Exception {




        //Every time the project is started, delete the data in the database.
        // Remember that the tables to be deleted have a dependency order,
        // and the data in the tables with the deepest dependency should be deleted first.
        changingRoleTypeRepository.deleteAll();
        emailChangingRepository.deleteAll();
        visitHistoryRepository.deleteAll();
        favoritesCourseRepository.deleteAll();
        videoCourseRepository.deleteAll();
        userRepository.deleteAll();


        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        //Add a new user
        String firstName = "leo";
        String lastName = "lee";
        String email = "jiayuan@brunel.ac.uk";
        String encryptedPassword = encoder.encode("leo_pass");

        User newUser = User.createANewUser(firstName, lastName, email, encryptedPassword);
        userRepository.save(newUser);
        logger.info("Added a new user , id is :" + newUser.getId());


        //Add five new courses
        VideoCourse videoCourse1 = new VideoCourse("Core Java Volume I – Fundamentals");
        VideoCourse videoCourse2 = new VideoCourse("Java: A Beginner’s Guide");
        VideoCourse videoCourse3 = new VideoCourse("Head First Java");
        VideoCourse videoCourse4 = new VideoCourse("Java - The Complete Reference");
        VideoCourse videoCourse5 = new VideoCourse("Java Concurrency in Practice");



        videoCourseRepository.save(videoCourse1);
        videoCourseRepository.save(videoCourse2);
        videoCourseRepository.save(videoCourse3);
        videoCourseRepository.save(videoCourse4);
        videoCourseRepository.save(videoCourse5);
        Iterable<VideoCourse> videoCourses = videoCourseRepository.findAll();

        videoCourses.forEach(System.out::println);

        //Add first three courses to the user's favorites folder
        FavoritesCourse favoritesCourse1 = new FavoritesCourse(newUser, videoCourse1);
        FavoritesCourse favoritesCourse2 = new FavoritesCourse(newUser, videoCourse2);
        FavoritesCourse favoritesCourse3 = new FavoritesCourse(newUser, videoCourse3);

        favoritesCourseRepository.save(favoritesCourse1);
        favoritesCourseRepository.save(favoritesCourse2);
        favoritesCourseRepository.save(favoritesCourse3);
        //Unfavorite the course number 2
        favoritesCourseRepository.deleteFavoritesCourseByUserAndVideoCourse(newUser, videoCourse2);
        //Show the courses in the favorites folder, should display 2 courses which are number 1 and number 2.
        List<FavoritesCourse> list = favoritesCourseRepository.findAllByUser(newUser);
        for (FavoritesCourse f : list) {
            System.out.println(f.getVideoCourse());
        }

        //Add 2 browsing records for this user
        VisitHistory visitHistory1 = new VisitHistory(newUser, videoCourse1);
        VisitHistory visitHistory2 = new VisitHistory(newUser, videoCourse2);
        visitHistoryRepository.save(visitHistory1);
        visitHistoryRepository.save(visitHistory2);
        //Display the browsing history for this user
        List<VisitHistory> vlist = visitHistoryRepository.findAllByUserOrderByIdDesc(newUser);
        for (VisitHistory v : vlist) {
            System.out.println(v.getVideoCourse());
        }

        //Add a role change apply for this user to become a teacher
//        ChangingRoleType changingRoleType = new ChangingRoleType(newUser, User.TYPE_TEACHER);
//        changingRoleTypeRepository.save(changingRoleType);

//        Optional<ChangingRoleType> thisApply = changingRoleTypeRepository.findChangingRoleTypeByUserAndApprovedTime(newUser, null);
//        System.out.println(thisApply);


    }
}
