import Controller from '@ember/controller';
import { action } from '@ember/object';
import { inject as service } from '@ember/service';
import $ from 'jquery';
import { tracked } from '@glimmer/tracking';
export default class TeacherexamController extends Controller {
  @tracked questions = [];
  @tracked i = 0;
  @tracked Q = '';
  @tracked O1 = '';
  @tracked O2 = '';
  @tracked O3 = '';
  @tracked O4 = '';
  @tracked ca = '';
  @tracked status = '';
  @service('userdetails') user;
  get qno() {
    return this.i + 1;
  }
  get status() {
    return this.status;
  }

  getQuestions() {
    //gets all the questions published by the teacher in a readonly mode.
    this.i = 0;
    this.status = '';

    var temp = this;
    var settings = {
      url: 'http://localhost:8081/Demmoapp/TeachergetQuestions',
      method: 'POST',
      timeout: 0,
      data: {
        topicId: this.user.getTopicID() + '',
        loginId: this.user.getloginID(),
      },
      success: (da) => {
        var data = JSON.parse(da);
        temp.questions = data;
        console.log('cons', temp.questions[0]);
        if (data.length > 0) {
          $('#questions').show();
          temp.Q = temp.questions[0].Question;
          temp.O1 = temp.questions[0].Option_1;
          temp.O2 = temp.questions[0].Option_2;
          temp.O3 = temp.questions[0].Option_3;
          temp.O4 = temp.questions[0].Option_4;
          temp.ca = temp.questions[0].Correct_ans;
        } else {
          temp.status = 'no Questions published';
        }
        // console.log(temp.Q, temp.O1);
      },
    };

    $.ajax(settings).done(function (response) {
      console.log(response);
    });
  }

  //   can be used for future reference
  //   @action
  //   addQuestion() {
  //     //to add some more questions to the topic.
  //     var temp = this;
  //     var settings = {
  //       url: 'http://localhost:8081/Demmoapp/AddQuestionInside',
  //       method: 'POST',
  //       timeout: 0,
  //       data: { topicId: this.model + '' },
  //       success: (d) => {
  //         var data = JSON.parse(d);
  //         console.log(data);
  //         console.log(data.topicName);
  //         temp.user.addTopic(data.topicName);
  //         console.log(temp.user.getTopic());
  //         temp.transitionToRoute('addquestion');
  //       },
  //     };

  //     $.ajax(settings).done(function (response) {
  //       console.log(response);
  //     });
  //   }
  //
  // @action
  // edit() {// disables readonly mode.
  //     console.log("hii");
  //     $('#Question').prop("disabled", false);
  //     $('#op1').prop("disabled", false);
  //     $('#op2').prop("disabled", false);
  //     $('#op3').prop("disabled", false);
  //     $('#op4').prop("disabled", false);
  //     $('#ca').prop("disabled", false);
  //     $('#submitedit').show();
  // }
  @action
  submit() {
    //takes the updated change sand sasve the changes in the data base.
    var json = {
      questionId: this.questions[this.i].Question_id + '',
      question: $('#Question').val(),
      op1: $('#op1').val(),
      op2: $('#op2').val(),
      op3: $('#op3').val(),
      op4: $('#op4').val(),
      ca: $('#ca').val(),
    };
    var temp = this;
    console.log(json);
    var settings = {
      url: 'http://localhost:8081/Demmoapp/Updatequestions',
      method: 'POST',
      timeout: 0,
      data: json,
      success: (data) => {
        var d = JSON.parse(data);
        if (d.status == 1) {
          temp.status = 'updated';
          $('#Question').prop('disabled', true);
          $('#op1').prop('disabled', true);
          $('#op2').prop('disabled', true);
          $('#op3').prop('disabled', true);
          $('#op4').prop('disabled', true);
          $('#ca').prop('disabled', true);
          $('#submitedit').hide();
        }
      },
    };

    $.ajax(settings).done(function (response) {
      console.log(response);
    });
  }
  @action
  previousQuestion() {
    //going to previous question.
    if (this.i > 0) {
      console.log(this.i, 'hii');
      this.Q = this.questions[this.i - 1].Question;
      this.O1 = this.questions[this.i - 1].Option_1;
      this.O2 = this.questions[this.i - 1].Option_2;
      this.O3 = this.questions[this.i - 1].Option_3;
      this.O4 = this.questions[this.i - 1].Option_4;
      //this.answered.removeObject(this.json);
      this.i -= 1;
      //console.log(this.answered);
      //this.send('showquestions');
      console.log(this.i);
    }
  }
  @action
  showQuestions() {
    //to go to next question.

    if (this.i + 1 < this.questions.length) {
      this.Q = this.questions[this.i + 1].Question;
      this.O1 = this.questions[this.i + 1].Option_1;
      this.O2 = this.questions[this.i + 1].Option_2;
      this.O3 = this.questions[this.i + 1].Option_3;
      this.O4 = this.questions[this.i + 1].Option_4;
      this.i += 1;
    } else {
      this.status = 'allquestions completed';
      $('#logout').show();
      $('#questions').hide();
      this.i = 0;
    }
  }

  @action
  goBack() {
    //redirects to home page
    this.transitionToRoute('teacherhome');
  }
}
