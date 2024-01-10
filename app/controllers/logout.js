import Controller from '@ember/controller';
import { action } from '@ember/object';
export default class LogoutController extends Controller {
  @action
  goHome() {
    //redirects to home page
    this.transitionToRoute('/');
  }
}
