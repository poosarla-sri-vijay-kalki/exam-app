import EmberRouter from '@ember/routing/router';
import config from 'exam-app/config/environment';

export default class Router extends EmberRouter {
  location = config.locationType;
  rootURL = config.rootURL;
}

Router.map(function () {
  this.route('login');
  this.route('registration');
  this.route('studenthome');
  this.route('studentexam', { path: 'studentexam/:studentexam_id' });
  this.route('teacherhome');
  this.route('teacherexam', { path: 'teacherexam/:teacherexam_id' });
  this.route('addquestion');
  this.route('logout');
});


