import Route from '@ember/routing/route';
import { action } from '@ember/object';
export default class RegistrationRoute extends Route {
    model() {
        console.log("regestration route");
    }
    @action
    willTransition() {
        console.log("changing from registration");
        this.controller.user = "";
        this.controller.password = "";
        this.controller.result = "";
    }
}
