import Controller from '@ember/controller';
import { tracked } from '@glimmer/tracking';
import $ from 'jquery';
import { action } from '@ember/object';
export default class RegistrationController extends Controller {
  @tracked user = '';
  @tracked password = '';
  @tracked result = '';
  @action
  regiester() {
    /// checks whethere the enter details are stored before and save the details .
    var temp = this;
    var uname = $('#uname').val();
    var pass = $('#pass').val();
    var rpass = $('#rpass').val();
    var profile = $('#profile').val();
    console.log(uname);
    console.log(pass);
    var settings = {
      url: 'http://localhost:8081/Demmoapp/Regester',
      method: 'POST',
      timeout: 0,
      data: { uname: uname, pass: pass, rpass: rpass, profile: profile },
      returntype: JSON,
      success: (d) => {
        var data = JSON.parse(d);
        if (data.status == 0) {
          temp.result = 'ENTER BOTH PASWORDS CORRECTLY!!';
          document.getElementById('myform').reset();
        } else if (data.status == 1) {
          temp.result = 'REGESTERED SUCESSFULLY click login !!';
          $('#userdetails').hide();
          document.getElementById('myform').reset();
        } else {
          temp.result = 'ALREADY A REGISTERED USER!!';
          document.getElementById('myform').reset();
        }
      },
      error: (err) => alert('error', err),
    };

    $.ajax(settings).done(function (response) {
      console.log(response);
    });
  }
}
