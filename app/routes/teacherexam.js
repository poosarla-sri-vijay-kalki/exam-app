import Route from '@ember/routing/route';
import { action } from '@ember/object';
import { inject as service } from '@ember/service';
export default class TeacherexamRoute extends Route {
    @service('userdetails') user;
    model(params) {
        console.log(params);
        const { teacherexam_id } = params;
        console.log("hfv");
        const controller = this.controllerFor('teacherexam');
        controller.getQuestions();
        if (this.user.getUname() === "") {
            this.transitionTo("login");
        }
        return teacherexam_id;
    }
    @action
    willTransition() {
        console.log("changing route from teacher exam");
        this.controller.questions = [];
        this.controller.i = 0;
        this.controller.Q = "";
        this.controller.O1 = "";
        this.controller.O2 = "";
        this.controller.O3 = "";
        this.controller.O4 = "";
        this.controller.ca = "";
        this.controller.status = "";
        console.log("status=", this.controller.status = "");
    }

}
