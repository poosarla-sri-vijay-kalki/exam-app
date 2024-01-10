import Route from '@ember/routing/route';
import { inject as service } from '@ember/service';
import { action } from '@ember/object';
export default class StudentexamRoute extends Route {
  @service('userdetails') user;
  model(params) {
    const { studentexam_id } = params;
    const controller = this.controllerFor('studentexam');
    controller.renderTime();
    controller.getquestions();
    if (this.user.getUname() === '') {
      this.transitionTo('login');
    }
    return studentexam_id;
  }
  @action
  willTransition() {
    console.log('changing from student exam');
    this.controller.questions = [];
    this.controller.i = 0;
    this.controller.answered = [];
    this.controller.Topic = '';
    this.controller.Q = '';
    this.controller.O1 = '';
    this.controller.O2 = '';
    this.controller.O3 = '';
    this.controller.O4 = '';
    this.controller.score = 0;
    this.controller.json = {};
    this.controller.status = '';
    clearInterval(this.controller.interval);
    this.controller.interval = 0;
    this.controller.duration = 0;
  }
}
