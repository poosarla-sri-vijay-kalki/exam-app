import Controller from '@ember/controller';
import $ from 'jquery';
import { tracked } from '@glimmer/tracking';
import { action } from '@ember/object';
import { inject as service } from '@ember/service';
export default class AddquestionController extends Controller {
  @tracked qno = 1;
  @tracked i = 0;
  @tracked status = '';
  @service('userdetails') user;
  get getI() {
    return this.i + 1;
  }
  topicName = null;
  questions = [];
  // method to check whether all the details of the questions are given correctly
  checkempty(json) {
    const regexp1 = /[a-zA-Z0-9]/;
    const regexp2 = /[1234]/;
    if (
      !regexp1.test(json.Question) ||
      !regexp1.test(json.op1) ||
      !regexp1.test(json.op2) ||
      !regexp1.test(json.op3) ||
      !regexp1.test(json.op4) ||
      !regexp2.test(json.co)
    ) {
      console.log(!regexp1.test(json.Question));
      console.log(!regexp1.test(json.op1));
      console.log(!regexp1.test(json.op2));
      console.log(!regexp1.test(json.op2));
      console.log(!regexp1.test(json.op3));
      console.log(!regexp2.test(json.co));
      return true;
    } else {
      return false;
    }
  }
  @action
  add() {
    //adds the questions continously
    console.log('Uname', this.user.getUname());
    console.log('TopicId', this.user.getTopicID());
    var json = {
      TopicId: this.user.getTopicID(),
      Question: $('#question').val(),
      op1: $('#op1').val(),
      op2: $('#op2').val(),
      op3: $('#op3').val(),
      op4: $('#op4').val(),
      co: $('#co').val(),
    };
    if (this.checkempty(json)) {
      this.status = 'enter the correct details';
      return;
    } else {
      console.log('choice=', json);
      this.status = '';
    }
    if (this.i < this.questions.length) {
      this.questions[this.i] = json;
      console.log('questions saved', this.questions);
      this.i += 1;

      if (this.i < this.questions.length) {
        console.log('I value=', this.i);
        $('#question').val(this.questions[this.i].Question);
        $('#op1').val(this.questions[this.i].op1);
        $('#op2').val(this.questions[this.i].op2);
        $('#op3').val(this.questions[this.i].op3);
        $('#op4').val(this.questions[this.i].op4);
        $('#co').val(this.questions[this.i].co);
      } else {
        $('#question1').trigger('reset');
      }
    } else {
      this.questions.addObject(json);
      this.i += 1;
      $('#question1').trigger('reset');
    }

    //this.questions.addObject(json);

    this.qno = this.qno + 1;
    console.log(this.questions);
    console.log('I value=', this.i);
  }

  // moving to previous question
  @action
  previous() {
    if (this.i > 0) {
      this.status = '';
      console.log('Uname', this.user.getUname());
      console.log('TopicId', this.user.getTopicID());
      var json = {
        TopicId: this.user.getTopicID(),
        Question: $('#question').val(),
        op1: $('#op1').val(),
        op2: $('#op2').val(),
        op3: $('#op3').val(),
        op4: $('#op4').val(),
        co: $('#co').val(),
      };
      if (!this.checkempty(json)) {
        if (this.i < this.questions.length) {
          this.questions[this.i] = json;
        } else {
          this.questions.addObject(json);
        }
      }

      this.i -= 1;
      if (this.i >= 0) {
        console.log(this.questions);
        $('#question').val(this.questions[this.i].Question);
        $('#op1').val(this.questions[this.i].op1);
        $('#op2').val(this.questions[this.i].op2);
        $('#op3').val(this.questions[this.i].op3);
        $('#op4').val(this.questions[this.i].op4);
        $('#co').val(this.questions[this.i].co);
      }
    }
    console.log('I value=', this.i);
  }
  // submitting all the questions
  @action
  submit() {
    //once all the questions are added the questions are saved
    var temp = this;
    $('#logout').show();
    console.log('Questions', this.questions);
    console.log('topic name', this.topicName);
    var questMap = {};
    this.questions.forEach((q, i) => (questMap[i] = JSON.stringify(q)));
    // questMap["Teacher"] = this.user.getuname();
    console.log('questmap', { questMap });
    var settings = {
      url: 'http://localhost:8081/Demmoapp/Question',
      method: 'POST',
      timeout: 0,
      data: questMap,
    };

    $.ajax(settings).done(function (response) {
      console.log(response);
      temp.questions.clear();
      $('#question1').hide();
      $('#result').replaceWith('Question Paper Registered');
    });
  }

  // redirects to back page
  @action
  goBack() {
    this.transitionToRoute('teacherhome');
  }
}
