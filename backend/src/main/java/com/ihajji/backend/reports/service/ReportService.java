package com.ihajji.backend.reports.service;

import java.util.List;
import java.util.Optional;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ihajji.backend.posts.entity.PostEntity;
import com.ihajji.backend.posts.repository.PostRepository;
import com.ihajji.backend.reports.Entity.ReportPostEntity;
import com.ihajji.backend.reports.Entity.ReportUserEntity;
import com.ihajji.backend.reports.dto.ReportDto;
import com.ihajji.backend.reports.dto.ReportErrDto;
import com.ihajji.backend.reports.repository.ReportPostRepository;
import com.ihajji.backend.reports.repository.ReportUserRepository;
import com.ihajji.backend.user.entity.UserEntity;
import com.ihajji.backend.user.repository.UserRepository;

@Service
public class ReportService {
    final ReportUserRepository reportUser;
    final ReportPostRepository reportPost;
    final UserRepository userRepo;
    final PostRepository postRepo;

    ReportService(ReportPostRepository reportPost, ReportUserRepository repportUser, UserRepository user,
            PostRepository postRepo) {
        this.reportPost = reportPost;
        this.reportUser = repportUser;
        this.userRepo = user;
        this.postRepo = postRepo;
    }

    public ReportErrDto reportPost(String username, ReportDto dto) {
        if (dto.getReason().isEmpty()) {
            return new ReportErrDto(HttpStatus.SC_BAD_REQUEST, "the reson is empty ");

        }
        Optional<UserEntity> reporter = userRepo.findByUsername(username);
        if (!reporter.isPresent()) {
            return new ReportErrDto(HttpStatus.SC_INTERNAL_SERVER_ERROR, "midleware is not working properlly");
        }
        Optional<PostEntity> reportedPost = postRepo.findById(dto.getPost_id());
        if (!reportedPost.isPresent()) {
            return new ReportErrDto(HttpStatus.SC_BAD_REQUEST, "the post is not exist ");
        }

        ReportPostEntity response = new ReportPostEntity();
        response.setPost(reportedPost.get());
        response.setReason(dto.getReason());
        response.setReporter(reporter.get());
        reportPost.save(response);

        return new ReportErrDto();
    }

    public ReportErrDto ReportUser(String username, ReportDto dto) {
        if (dto.getReason().isEmpty()) {
            return new ReportErrDto(HttpStatus.SC_BAD_REQUEST, "the reson is empty ");

        }
        Optional<UserEntity> reporter = userRepo.findByUsername(username);
        if (!reporter.isPresent()) {
            return new ReportErrDto(HttpStatus.SC_INTERNAL_SERVER_ERROR, "midleware is not working properlly");
        }
        Optional<UserEntity> reported = userRepo.findByUsername(dto.getUsername());
        if (!reported.isPresent()) {
            return new ReportErrDto(HttpStatus.SC_BAD_REQUEST, "the user is not exist ");
        }

        ReportUserEntity response = new ReportUserEntity();
        response.setReported(reported.get());;
        response.setReason(dto.getReason());
        response.setReporter(reporter.get());
        reportUser.save(response);

        return new ReportErrDto();
    }
    public List<ReportPostEntity> GetPostReports(){
        return this.reportPost.findAll();

    }
     public List<ReportUserEntity> GetUserReports(){
        return this.reportUser.findAll();

    }


}
