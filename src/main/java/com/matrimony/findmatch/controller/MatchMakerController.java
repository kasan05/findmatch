package com.matrimony.findmatch.controller;

import com.matrimony.findmatch.dto.*;
import com.matrimony.findmatch.exception.FileUploadException;
import com.matrimony.findmatch.exception.UserNotLoginException;
import com.matrimony.findmatch.modal.*;
import com.matrimony.findmatch.service.AppAwsS3Service;
import com.matrimony.findmatch.service.FileService;
import com.matrimony.findmatch.service.FriendshipService;
import com.matrimony.findmatch.service.MatchMakerService;
import com.matrimony.findmatch.util.CookieHelper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Year;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/match-maker")
public class MatchMakerController {

    private final MatchMakerService matchMakerService;
    private final FileService fileService;
    private final CookieHelper cookieHelper;
    private FriendshipService friendshipService;
    private final AppAwsS3Service appAwsS3Service;

    public MatchMakerController(final MatchMakerService matchMakerService,
                                FileService fileService,
                                CookieHelper cookieHelper,
                                FriendshipService friendshipService,
                                AppAwsS3Service appAwsS3Service){
        this.matchMakerService = matchMakerService;
        this.fileService = fileService;
        this.cookieHelper = cookieHelper;
        this.friendshipService = friendshipService;
        this.appAwsS3Service = appAwsS3Service;
    }

    @GetMapping("/personnel-data")
    public ResponseEntity<PersonalData> getAllProfession(){
        List<String> professions = Profession.getAll();
        List<String> genders = Gender.getAll();
        List<String> religions = Religion.getAll();
        List<String> castes = Caste.getAll();
        List<String> countries = Country.getAll();
        List<String> maritalStatuses = MaritalStatus.getAll();

        CacheControl cacheControl = CacheControl.maxAge(10,TimeUnit.MINUTES).cachePublic();
        return  ResponseEntity.ok()
                .cacheControl(cacheControl)
                .body(new PersonalData(
                        professions,
                        religions,
                        castes,
                        countries,
                        genders,
                        maritalStatuses
                ));
    }

    @GetMapping
    public ResponseEntity<List<MatchMaker>> getAllMatchMakers(){
        return new ResponseEntity<>(matchMakerService.getAll(),HttpStatus.OK);
    }

    @PostMapping("/profile")
    public ResponseEntity<MatchMakerProfileDTO> getProfile(HttpServletRequest request) throws IOException {
        Optional<String> userIdOp = cookieHelper.getUserId(request);
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        if(userIdOp.isPresent()){
            MatchMakerDto matchMakerDto = matchMakerService.get(Long.parseLong(userIdOp.get()));

            List<Resource> resources= fileService.getFilesByUserId(userIdOp.get());

//            List<Resource> resources= appAwsS3Service.getFilesByUserId(userIdOp.get(),AwsBucketType.PHOTOS);
            List<String> images = resources.stream()
                    .map(resource-> {
                        try {
                            return convertToBase64(resource);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }).toList();
            MatchMakerProfileDTO matchMakerProfileDTO = new MatchMakerProfileDTO(images,matchMakerDto);
            return new ResponseEntity<MatchMakerProfileDTO>(matchMakerProfileDTO,HttpStatus.OK);

        }else{
            return new ResponseEntity<MatchMakerProfileDTO>(HttpStatus.NO_CONTENT);
        }
    }
    @PostMapping(value = "/profile/data")
    public ResponseEntity<MatchMakerDto> getProfileData(HttpServletRequest request){
        Optional<String> userIdOp = cookieHelper.getUserId(request);
        return userIdOp.map(string ->
                new ResponseEntity<>(matchMakerService.get(Long.parseLong(string)), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @PostMapping(value = "/profile/images",produces = "application/zip")
    public void getProfile2(HttpServletRequest request,
                                                             HttpServletResponse response) throws IOException {
        Optional<String> userIdOp = cookieHelper.getUserId(request);
        if(userIdOp.isPresent()) {
            //MatchMakerDto matchMakerDto = matchMakerService.get(Long.parseLong(userIdOp.get()));
            response.setContentType("application/zip");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"images.zip\"");
             fileService.getFilesByUserId2(response.getOutputStream(),userIdOp.get());
        }else{
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("error");
        }
    }
    @PostMapping("/profile/friend")
    public ResponseEntity<MatchMakerProfileDTO> getProfileOfFriend(HttpServletRequest request,
                                                                   @RequestBody String id) throws IOException {
        Optional<String> userIdOp = cookieHelper.getUserId(request);
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        if(userIdOp.isPresent()){
            if(id==null || id.isEmpty() || !friendshipService.isAnyFriendship(Long.parseLong(userIdOp.get()),Long.parseLong(id))){
                return new ResponseEntity<MatchMakerProfileDTO>(HttpStatus.NO_CONTENT);
            }
            MatchMakerDto matchMakerDto = matchMakerService.get(Long.parseLong(id));

            List<Resource> resources= fileService.getFilesByUserId(id);

//            List<Resource> resources= appAwsS3Service.getFilesByUserId(userIdOp.get(),AwsBucketType.PHOTOS);
            List<String> images = resources.stream()
                    .map(resource-> {
                        try {
                            return convertToBase64(resource);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }).toList();
            MatchMakerProfileDTO matchMakerProfileDTO = new MatchMakerProfileDTO(images,matchMakerDto);
            return new ResponseEntity<MatchMakerProfileDTO>(matchMakerProfileDTO,HttpStatus.OK);

        }else{
            return new ResponseEntity<MatchMakerProfileDTO>(HttpStatus.NO_CONTENT);
        }
    }
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AppResponse<String>> addAdditionalDetails(@Valid @ModelAttribute MatchMakerDto matchMakerDto, HttpServletRequest request) {
        Cookie[] cookieArray = request.getCookies();
        for(Cookie cookie:cookieArray){
            if(cookie.getName().equals("C_ST")){
                String userId = cookie.getValue();
                if (userId == null || userId.isEmpty()) {
                    throw new UserNotLoginException();
                }
                matchMakerDto.setId(Long.parseLong(userId));
            }
        }
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        CompletableFuture<Void> fileCompletableFuture = CompletableFuture.runAsync(
                () -> {

                    try {
                        fileService.uploadPhotoAlbum(matchMakerDto.getMultipartFiles(),String.valueOf(matchMakerDto.getId()));
                        //appAwsS3Service.upload(matchMakerDto.getMultipartFiles(),String.valueOf(matchMakerDto.getId()),AwsBucketType.PHOTOS);
                    } catch (Exception e) {
                        throw new FileUploadException(e.getMessage());
                    }
                },executor
        );

        CompletableFuture<Void> dbCompletableFuture = CompletableFuture.runAsync(
                ()->{
                    matchMakerService.saveAdditionalDetails(matchMakerDto);
                },executor
        );

        CompletableFuture<?> tasks = CompletableFuture.allOf(fileCompletableFuture,dbCompletableFuture)
                .handle((result,ex)->{
                    if(ex!=null){
//                        if(fileCompletableFuture.isCompletedExceptionally()){
//
//                        }else if(dbCompletableFuture.isCompletedExceptionally()){
//
//                        }
                        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                    }

                    return result;
                });
        tasks.join();
        executor.shutdown();
        return new ResponseEntity<AppResponse<String>>(new AppResponse<String>("success","success"),HttpStatus.OK);
    }

    @PostMapping("/data")
    public ResponseEntity<SearchResultsDTO> getAllMatchMakerForSearch(HttpServletRequest httpServletRequest,
                                                                      @RequestBody PageDTO pageDTO){
        Cookie[] cookieArray = httpServletRequest.getCookies();
        String userId="";
        for(Cookie cookie:cookieArray){
            if(cookie.getName().equals("C_ST")){
                 userId = cookie.getValue();
                if (userId == null || userId.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
        FilterInput filterInput = pageDTO.filterInput();
        Pageable pageable = PageRequest.of(pageDTO.offset(),pageDTO.limit());
        List<String> selectedCountries = filterInput.selectedCountriesTemp();
        List<String> selectedProfessions = filterInput.selectedProfessionsTemp();
        List<Country> countriesEnumList = null;
        List<Profession> professionsEnumList = null;
        Page<UserSearchDTO> userSearchDTOS = null;
        int x=0;
        LocalDate startDate = null;
        LocalDate endDate = null;
                List<String> ageList = filterInput.ageArr();
        if(ageList!=null && ageList.size()==2){
            int start = Integer.parseInt(ageList.get(0));
             startDate = LocalDate.of(Year.now().getValue()-start,1,1);
            int end = Integer.parseInt(ageList.get(1));
             endDate =LocalDate.of(Year.now().getValue()-end,1,1);
        }else{
            x++;
        }

        if(selectedCountries!=null && !selectedCountries.isEmpty()){
            countriesEnumList =selectedCountries.stream().map(s->Country.getByValue(s).orElse(null)).toList();
        }else{
            x++;
        }
        if(selectedProfessions!=null && !selectedProfessions.isEmpty()){
            professionsEnumList = selectedProfessions.stream().map(p->Profession.getByValue(p).orElse(null)).toList();
        }else{
            x++;
        }
        if(x==3){
            userSearchDTOS = matchMakerService.getAllForSearchIfNoFilter(pageable);
        }else{
            userSearchDTOS = matchMakerService.getAllForSearch(pageable,countriesEnumList,professionsEnumList,startDate,
                    endDate);
        }
        Map<Long,String> friendIdsAndStatus= friendshipService.findFriendsByUserId(Long.valueOf(userId))
                        .stream().collect(Collectors.toMap(UserSearchDTO::getId,
                        UserSearchDTO::getFriendRequestStatus));
        final String uId = userId;
        List<UserSearchDTO> list = userSearchDTOS.getContent().stream().map(
                usDtos->{
                                    if(friendIdsAndStatus.containsKey(usDtos.getId())){
                                        usDtos.setFriendRequestStatus(friendIdsAndStatus.get(usDtos.getId()));
                                    }
                                    return usDtos;
                                    }).filter(uDtos->!String.valueOf(uDtos.getId()).equals(uId))
                                    .toList();

        return new ResponseEntity<SearchResultsDTO>(new SearchResultsDTO(list,
                userSearchDTOS.getTotalElements(),
                userSearchDTOS.getNumber(),
                pageDTO.limit()
                ), HttpStatus.OK);
    }
    private String convertToBase64(Resource resource) throws IOException {
        byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
        return Base64.getEncoder().encodeToString(bytes);
    }

}
