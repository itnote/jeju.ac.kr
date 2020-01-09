package com.infomind.axboot.controllers;

import com.chequer.axboot.core.api.response.Responses;
import com.chequer.axboot.core.controllers.BaseController;
import com.chequer.axboot.core.parameter.RequestParams;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import com.chequer.axboot.core.api.response.ApiResponse;
import org.springframework.web.bind.annotation.*;
import com.infomind.axboot.domain.image.Image;
import com.infomind.axboot.domain.image.ImageService;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/api/v1/image")
public class ImageController extends BaseController {

    @Autowired
    private ImageService imageService;


    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.ListResponse list(RequestParams requestParams) {
        List list = imageService.gets(requestParams);
        return Responses.ListResponse.of(list);
    }
    @RequestMapping(method = {RequestMethod.PUT}, produces = APPLICATION_JSON)
    public Image save(@RequestBody Image request) {
        imageService.save(request);
        return request;
    }

    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Object findOne(@PathVariable Long id){
        return imageService.findOne(id);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, produces = APPLICATION_JSON)
    public Image upload(
            @RequestParam(value = "file") MultipartFile multipartFile,
            @RequestParam(value = "thumbnail", required = false, defaultValue = "false") boolean thumbnail,
            @RequestParam(value = "thumbnailWidth", required = false, defaultValue = "60") int thumbnailWidth,
            @RequestParam(value = "thumbnailHeight", required = false, defaultValue = "60") int thumbnailHeight) throws IOException {

        Image image = new Image();
        image.setMultipartFile(multipartFile);
        imageService.upload(image);

        return image;
    }

    @RequestMapping(value = "/preview", method = RequestMethod.GET)
    public void preview(HttpServletResponse response, @RequestParam Long id) throws IOException {
        imageService.preview(response, id);
    }

    @RequestMapping(value = "/thumbnail", method = RequestMethod.GET)
    public void thumbnail(HttpServletResponse response, @RequestParam Long id) throws IOException {
        imageService.thumbnail(response, id);
    }

//    @RequestMapping(method = RequestMethod.DELETE)
//    public void delete(@RequestParam Long id) {
//        imageService.deleteById(id);
//    }


}