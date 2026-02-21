package com.ihajji.backend.admin.service;

import java.util.List;
import java.util.Optional;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ihajji.backend.admin.dto.AdminDataDto;
import com.ihajji.backend.admin.dto.AdminErrorDto;
import com.ihajji.backend.admin.dto.AdminPostReportDto;
import com.ihajji.backend.admin.dto.AdminUserDto;
import com.ihajji.backend.admin.dto.AdminUserReportDto;
import com.ihajji.backend.notification.repository.NotificationRepository;
import com.ihajji.backend.posts.entity.PostEntity;
import com.ihajji.backend.posts.repository.CommentRepository;
import com.ihajji.backend.posts.repository.PostRepository;
import com.ihajji.backend.reports.repository.ReportPostRepository;
import com.ihajji.backend.reports.repository.ReportUserRepository;
import com.ihajji.backend.reports.service.ReportService;
import com.ihajji.backend.user.entity.UserEntity;
import com.ihajji.backend.user.repository.UserRepository;
import com.ihajji.backend.user.service.UserService;

@Service
public class AdminService {
    final UserRepository userrepo;
    final PostRepository postrepo;
    final CommentRepository commentRepo;
    final ReportPostRepository reportPostRepo;
    final ReportService ReportService;
    
  
    final UserService UserService;
      final ReportUserRepository ReportUser;
      final NotificationRepository not;
    AdminService(
            UserRepository userrepo,
            PostRepository postrepo,
            CommentRepository commentRepo,
            ReportPostRepository reportPostRepo,
            ReportService ReportService,
            UserService UserService, ReportUserRepository ReportUser, NotificationRepository not){
        this.postrepo= postrepo;
        this.userrepo = userrepo;
        this.commentRepo = commentRepo;
        this.reportPostRepo = reportPostRepo;
        this.ReportService = ReportService;
        this.UserService = UserService;
        this.ReportUser = ReportUser;
        this.not = not;
    }
    @Transactional
    public AdminErrorDto BannedUser(AdminUserDto dto){
        Optional<UserEntity> user =this.userrepo.findByUsername(dto.getUsername());
        if (!user.isPresent()){
            return new AdminErrorDto(HttpStatus.SC_BAD_REQUEST, "the user can't be found");

        }
        if (user.get().getRole().equals("ADMIN")){
             return new AdminErrorDto(HttpStatus.SC_BAD_REQUEST, "Admin Can't be baned");
        }
        user.get().setIs_baned(true);
      this.ReportUser.deleteByReported(user.get());
        userrepo.save(user.get());
        return new AdminErrorDto();


    }
    public AdminErrorDto UnBannedUser(AdminUserDto dto){
        Optional<UserEntity> user = this.userrepo.findByUsername(dto.getUsername());
        if (!user.isPresent()){
            return new AdminErrorDto(HttpStatus.SC_BAD_REQUEST, "the user can't be found");
        }
        user.get().setIs_baned(false);
        userrepo.save(user.get());
        return new AdminErrorDto();
    }

    @Transactional
    public AdminErrorDto DeletePost(Long dto){
        Optional<PostEntity> post = this.postrepo.findById(dto);
        if (!post.isPresent()){
            return new AdminErrorDto(HttpStatus.SC_BAD_REQUEST, "the post can't be found");
        }

        this.reportPostRepo.deleteByPostId(dto);
        this.commentRepo.deleteByPostId(dto);
        this.postrepo.delete(post.get());

        return new AdminErrorDto();
    }

    @Transactional(readOnly = true)
    public AdminDataDto Load(){
        List<AdminPostReportDto> postReports = this.ReportService.GetPostReports()
                .stream()
                .map(report -> new AdminPostReportDto(
                        report.getId(),
                        report.getReason(),
                        report.getTime(),
                        report.getPost().getId(),
                        report.getPost().getTitle(),
                        report.getPost().getUser().getUsername(),
                        report.getReporter().getUsername()))
                .toList();

        List<AdminUserReportDto> userReports = this.ReportService.GetUserReports()
                .stream()
                .map(report -> new AdminUserReportDto(
                        report.getId(),
                        report.getReason(),
                        report.getTime(),
                        report.getReported().getUsername(),
                        report.getReporter().getUsername()))
                .toList();

        return new AdminDataDto(
                this.UserService.BannedUsers(),
                this.UserService.UnBannedUsers(),
                this.UserService.GetUsers(),
                postReports,
                userReports);

    }
    @Transactional
    public AdminErrorDto delet(String username , String Adminuser){
         Optional<UserEntity>  Admin = this.userrepo.findByUsername(Adminuser);
        if (!Admin.isPresent()){
            return  new AdminErrorDto(HttpStatus.SC_BAD_REQUEST, "the user can't be found");
        }


        Optional<UserEntity> user = this.userrepo.findByUsername(username);
        if (!user.isPresent()){
            return  new AdminErrorDto(HttpStatus.SC_BAD_REQUEST, "the user can't be found");
        }
        this.reportPostRepo.deleteByReporter(user.get());
        this.ReportUser.deleteByReported(user.get());
        this.ReportUser.deleteByReporter(user.get());
this.not.deleteByReceiver(user.get());
this.not.deleteBySender(user.get());
        this.userrepo.delete(user.get());
        return new AdminErrorDto();

    }
    
}
