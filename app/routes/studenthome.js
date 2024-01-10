import Route from '@ember/routing/route';
import { inject as service } from '@ember/service';
import { action } from '@ember/object';
export default class StudenthomeRoute extends Route {
  @service('userdetails') user;
  model(args) {
    console.log('Student home route');
    if (this.user.getUname() === '') {
      this.transitionTo('login');
    }
    const controller = this.controllerFor('studenthome');
    controller.getVote(null);
  }
  @action
  willTransition() {
    console.log('changing route from student home----->');
    this.controller.topics = [];
    this.controller.history = [];
    this.controller.historystatus = '';
    this.controller.status = '';
  }
}
