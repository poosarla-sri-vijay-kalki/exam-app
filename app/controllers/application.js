// import countdown from 'countdownjs';
import Controller from '@ember/controller';
import { action } from '@ember/object';
import { inject as service } from '@ember/service';
// import { tracked } from '@glimmer/tracking';
export default class ApplicationController extends Controller {
  @service('userdetails') user;
  get islogin() {
    if (this.user.getUname() != '') return true;
  }

  @action
  logout() {
    this.user.clearAll();
    console.log('clearing the cache');
    this.transitionToRoute('logout');
  }
}
