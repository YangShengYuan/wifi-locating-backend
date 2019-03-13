package cloud.model.classroom;

import cloud.controller.BaseController;
import cloud.controller.Result;
import cloud.model.section.SectionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class ClassroomController extends BaseController {

    @Resource
    private ClassroomService classroomService;

    @Resource
    private SectionService sectionService;

    @GetMapping(value = { "/classroom/all" })
    public Result findAll() {
        return new Result("SUCCESS", "find all classrooms", classroomService.findAll());
    }

    @PostMapping(value = { "/classroom/create" })
    public Result create(@ModelAttribute Classroom classroom) {

        if (isEmpty(classroom.roomName)) {
            return new Result("FAIL", "Room name cannot be empty");
        }

        if (isEmpty(classroom.macAddress)) {
            return new Result("FAIL", "MAC address cannot be empty");
        }

        if (isEmpty(classroom.deskType)) {
            return new Result("FAIL", "Desk type cannot be empty");
        }

        if (classroom.capacity < 0) {
            return new Result("FAIL", "Room capacity cannot less than 0");
        }

        if (classroomService.existByRoomName(classroom.roomName)) {
            return new Result("FAIL", "Room name already exists");
        }

        classroomService.save(classroom);
        return new Result("SUCCESS", "Create classroom", classroom);
    }

    @PostMapping(value = { "/classroom/updateByRoomName" })
    public Result updateByRoomName(@ModelAttribute Classroom newClassroom) {

        if (!classroomService.existByRoomName(newClassroom.roomName)) {
            return new Result("FAIL", "The classroom does not exist");
        }

        Classroom oldClassroom = classroomService.findByRoomName(newClassroom.roomName);

        if (!isEmpty(newClassroom.macAddress)) {
            oldClassroom.setMacAddress(newClassroom.macAddress);
        }

        if (!isEmpty(newClassroom.deskType)) {
            oldClassroom.setDeskType(newClassroom.deskType);
        }

        if (newClassroom.capacity >= 0) {
            oldClassroom.setCapacity(newClassroom.capacity);
        }

        return new Result("SUCCESS", "Update classroom", newClassroom);
    }

    @GetMapping(value = { "/classroom/delete/{roomName}" })
    public Result deleteByRoomName(@PathVariable("roomName") String roomName) {

        if (isEmpty(roomName)) {
            return new Result("FAIL", "roomName cannot be empty");
        }

        if (!classroomService.existByRoomName(roomName)) {
            return new Result("FAIL", "The classroom does not exist");
        }

        classroomService.deleteByRoomName(roomName);
        return new Result("SUCCESS", "Delete classroom");
    }

    @GetMapping(value = { "/classroom/get/{roomName}" })
    public Result get(@PathVariable("roomName") String roomName) {

        if (isEmpty(roomName)) {
            return new Result("FAIL", "roomName cannot be empty");
        }

        if (!classroomService.existByRoomName(roomName)) {
            return new Result("FAIL", "The classroom does not exist");
        }

        Classroom classroom = classroomService.findByRoomName(roomName);
        return new Result("SUCCESS", "Get classroom detail", classroom);
    }

    @GetMapping(value = { "/classroom/get/{macAddress}" })
    public Result get(@PathVariable("macAddress") String macAddress) {

        if (isEmpty(macAddress)) {
            return new Result("FAIL", "macAddress cannot be empty");
        }

        if (!classroomService.existByMacAddress(macAddress)) {
            return new Result("FAIL", "The classroom does not exist");
        }

        Classroom classroom = classroomService.findByRoomName(macAddress);
        return new Result("SUCCESS", "Get classroom detail", classroom);
    }

    @GetMapping(value = { "/classroom/getWeeklySchedule/{roomName}" })
    public Result getWeeklySchedule(@PathVariable("roomName") String roomName) {

        if (isEmpty(roomName)) {
            return new Result("FAIL", "roomName cannot be empty");
        }

        if (!classroomService.existByRoomName(roomName)) {
            return new Result("FAIL", "Classroom not exist");
        }

        String[][] weeklySchedule = classroomService.getWeeklySchedule(roomName);
        return new Result("SUCCESS", "Get classroom detail", weeklySchedule);
    }

}
