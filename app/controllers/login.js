import Controller from '@ember/controller';
import { action } from '@ember/object';
import { tracked } from '@glimmer/tracking';
import { inject as service } from '@ember/service';
import $ from 'jquery';
export default class LoginController extends Controller {
  @tracked result = '';
  @tracked date1 = '';

  @service('userdetails') user;

  // checks the entered login details and redirects
  // to page based on profile
  @action
  checkLogin() {
    console.log('checking the entered login details---->');
    var temp = this;
    var uname = $('#uname').val();
    var pass = $('#pass').val();
    var profile = $('#profile').val();
    document.getElementById('myForm').reset();
    const success = (d) => {
      var data = JSON.parse(d);
      console.log('checking successfully completed');
      if (data.status) {
        var p = Number(data.profile);
        temp.user.addUser(uname);
        temp.user.addProfile(profile);
        temp.user.setloginID(data.loginId);
        if (p === 0) {
          console.log('redirecting to student home page');
          this.transitionToRoute('studenthome');
        } else {
          console.log('redirecting to teacher home page');
          this.transitionToRoute('teacherhome');
        }
      } else {
        temp.result = 'Invalid Credentials!!';
      }
    };

    const error = (err) => alert('error', err);

    var settings = {
      url: 'http://localhost:8081/Demmoapp/Login',
      method: 'POST',
      timeout: 0,
      data: { uname: uname, pass: pass, profile: profile },
      returntype: JSON,
      success,
      error,
    };
    $.ajax(settings).done(function (response) {});
  }

  @action
  goHome() {
    //redirects to home page
    this.transitionToRoute('/');
  }
}
