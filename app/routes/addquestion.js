import Route from '@ember/routing/route';
import { inject as service } from '@ember/service';
import { action } from '@ember/object';
export default class AddquestionRoute extends Route {
    @service('userdetails') user;
    model(params) {
        console.log('Add Questions Route');
        if (this.user.getUname() === "") {
            this.transitionTo("login");
        }
        return {};
    }
    @action
    willTransition() {
        console.log(' changing from Add Questions Route');
        this.controller.qno = 1;
        this.controller.i = 0;
        this.controller.status = "";
    }
}
