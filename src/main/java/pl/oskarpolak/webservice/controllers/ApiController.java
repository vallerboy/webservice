package pl.oskarpolak.webservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.oskarpolak.webservice.models.UserModel;
import pl.oskarpolak.webservice.models.repositories.UserRepository;

import javax.transaction.Transactional;
import javax.xml.ws.Response;
import java.util.Optional;

@Controller()
public class ApiController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/api/user/{nick}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserModel> getUser(@PathVariable("nick") String nick) {
        Optional<UserModel> model = userRepository.findByNick(nick);
        return model.map(userModel -> new ResponseEntity<>(userModel, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @RequestMapping(value = "/api/user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUsers(@RequestHeader("App-Password") String password) {
        if(!password.equals("tajnehaslo")){
            return new ResponseEntity("Bad credentiales", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/user", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addUser(@RequestBody UserModel model){
         if(userRepository.existsByNick(model.getNick())){
             return new ResponseEntity("User already exist", HttpStatus.BAD_REQUEST);
         }

         userRepository.save(model);
         return new ResponseEntity(HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/api/user/{nick}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity removeUser(@PathVariable("nick") String nick){
        userRepository.deleteByNick(nick);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/checkuser/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity checkUser(@RequestBody UserModel model){
        if(model.getPassword().isEmpty() || model.getNick().isEmpty()){
            return new ResponseEntity("Nick and password required", HttpStatus.BAD_REQUEST);
        }

        Optional<UserModel> userModelFormDatabase = userRepository.findByNick(model.getNick());
        if(!userModelFormDatabase.isPresent()){
            return new ResponseEntity("User not exist", HttpStatus.BAD_REQUEST);
        }
        if(!userModelFormDatabase.get().getPassword().equals(model.getPassword())){
            return new ResponseEntity("Password do not match", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("OK", HttpStatus.OK);

    }

}
