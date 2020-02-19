package com.zenghui.tmall_springboot.controller;

import com.zenghui.tmall_springboot.entity.Property;
import com.zenghui.tmall_springboot.service.PropertyService;
import com.zenghui.tmall_springboot.util.PageNavigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class PropertyController {
    @Autowired
    PropertyService propertyService;

    /**
     *
     * @param cid   根据cid找到对应的Category 然后findByCategory
     * @param start
     * @param size
     * @return PageNavigator page,是一个分页工具对象
     */
    @GetMapping("/categories/{cid}/properties")
    public PageNavigator list(@PathVariable("cid") int cid, @RequestParam(value = "start",defaultValue = "0") int start,
                              @RequestParam(value = "size",defaultValue = "5") int size){
        start = start<0?0:start;
        PageNavigator<Property> page = propertyService.list(cid,start,size,7);
        return page;
    }

    @GetMapping("/properties/{id}")
    public Property get(@PathVariable("id") int id) throws Exception{
        Property property =  propertyService.get(id);
        return property;
    }

    @PostMapping("/properties")
    public Property add(@RequestBody Property property){
        propertyService.add(property);
        return property;
    }

    @DeleteMapping("/properties/{id}")
    public String delete(@PathVariable("id") int id){
        propertyService.delete(id);
        return null;
    }

    @PutMapping("/properties")
    public Property update(@RequestBody Property property){
        propertyService.update(property);
        return property;
    }

}
