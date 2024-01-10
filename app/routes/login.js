import Route from '@ember/routing/route';
import { inject as service } from '@ember/service';
import { action } from '@ember/object';
export default class LoginRoute extends Route {
  @service('userdetails') user;
  model() {
    // this.user.clearAll();
  }
  @action
  willTransition() {
    console.log('chainging from login---->');
    this.controller.result = '';
  }
}
