import Route from '@ember/routing/route';
import { action } from '@ember/object';
import { inject as service } from '@ember/service';
export default class TeacherhomeRoute extends Route {
  @service('userdetails') user;
  model(params) {
    console.log('Teacher Home Route');
    if (this.user.getUname() === '') {
      this.transitionTo('login');
    }
    return params;
  }
  @action
  willTransition() {
    console.log('changing route from teacher home');
    this.controller.topicName = '';
    this.controller.des = '';
    this.controller.nofailteacher = [];
    this.controller.topics = [];
    this.controller.noStudentTopic = [];
    this.controller.failtopics = [];
    this.controller.goodteachers = [];
    this.controller.bestTeacher = { uname: '', percent: '' };

    this.controller.allTopicStatus = '';
    this.controller.NotAnsTopicstatus = '';
    this.controller.status = '';
    this.controller.bestTeacherStatus = '';
    this.controller.metric1 = '';
    this.controller.metric2 = '';
    this.controller.failTopicstatus = '';

    this.controller.fastofall = [];
    this.controller.totalSpeed = '';

    this.controller.fastoftopics = [];
    this.controller.SpeedByTopic = '';

    this.controller.fastofteachers = [];
    this.controller.SpeedByTeacher = '';
    this.controller.teacherbyVotes = [];
    this.controller.teacherbyVoteStatus = '';
  }
  resetController() {
    console.log('reseting it!');
  }
}
