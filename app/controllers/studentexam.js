import Controller from '@ember/controller';
import { inject as service } from '@ember/service';
import $ from 'jquery';
import { tracked } from '@glimmer/tracking';
import { action } from '@ember/object';
export default class StudentexamController extends Controller {
  @service('userdetails') user;
  @tracked questions = [];
  @tracked i = 0;
  @tracked answered = [];
  @tracked Topic = '';
  @tracked Q = '';
  @tracked O1 = '';
  @tracked O2 = '';
  @tracked O3 = '';
  @tracked O4 = '';
  @tracked score = 0;
  @tracked json = {};
  @tracked status = '';
  @tracked date1 = '';
  @tracked duration = '';
  interval = 0;
  get qno() {
    return this.i + 1;
  }

  @action
  getquestions() {
    //gets all the questions for the given topic.
    var data = [];
    var temp = this;
    var id = this.user.getTopicID();
    console.log('Get question ---------->', id);
    var settings = {
      url: 'http://localhost:8081/Demmoapp/getquestions',
      method: 'POST',
      timeout: 0,
      data: { id: id },
      error: (err) => console.log(`Errror`, err),
      success: (res) => {
        data = JSON.parse(res);
        console.log('number of questions=' + data.length);
        if (data.length == 0) {
          console.log('no questions');
          this.status = 'No Questions';
          $('#prevNextButtons').hide();
        } else {
          //console.log(data);
          $('#prevNextButtons').show();
          temp.questions = data;
          console.log(temp.questions[0]);
          // temp.qdetails.update(data);
          //var ques = temp.qdetails.questions;
          temp.Q = temp.questions[0].Question;
          temp.O1 = temp.questions[0].Option_1;
          temp.O2 = temp.questions[0].Option_2;
          temp.O3 = temp.questions[0].Option_3;
          temp.O4 = temp.questions[0].Option_4;
          $('#qdis').show();
        }
      },
    };
    $.ajax(settings).done(function (response) {
      console.log(response);
    });
  }
  renderTime(temp) {
    console.log('rendering', temp);
    let startTime = new Date();
    let duration = 0;
    this.interval = setInterval(() => {
      let curentTime = new Date();
      let h = curentTime.getHours();
      let m = curentTime.getMinutes();
      let s = curentTime.getSeconds();
      duration = (curentTime.getTime() - startTime.getTime()) / 1000;
      duration = Math.floor(duration);
      this.duration = duration;
      console.log('dureation=' + this.duration);
      m = Math.floor(duration / 60);
      s = duration % 60;
      if (m < 10) {
        m = '0' + m;
      }
      if (s < 10) {
        s = '0' + s;
      }
      this.date1 = m + ':' + s;
    }, 1000);
    console.log('interval', this.interval);
  }
  // let sh = startTime.getHours();
  // let sm = startTime.getMinutes();
  // let ss = startTime.getSeconds();

  @action
  showquestions() {
    //shows the question and save the answer given by the student.
    var choices, choice;
    choices = document.getElementsByName('qops');
    for (var i = 0; i < choices.length; i++) {
      if (choices[i].checked) {
        choice = choices[i].value;
      }
    }
    if (choice == null) {
      this.status = 'enter any option';
      return;
    } else {
      console.log('choice=', choice);
      this.status = '';
    }
    this.json = {
      id: String(this.questions[this.i].Question_Id),
      ans_choice: choice,
    };
    if (this.i < this.answered.length) {
      this.answered[this.i] = this.json;
    } else {
      this.answered.addObject(this.json);
    }
    console.log(this.answered);
    document.getElementById('questions').reset();
    if (this.i + 1 < this.questions.length) {
      this.Q = this.questions[this.i + 1].Question;
      this.O1 = this.questions[this.i + 1].Option_1;
      this.O2 = this.questions[this.i + 1].Option_2;
      this.O3 = this.questions[this.i + 1].Option_3;
      this.O4 = this.questions[this.i + 1].Option_4;
      if (this.i + 1 < this.answered.length) {
        $('#op' + this.answered[this.i + 1].ans_choice).prop('checked', true);
      }
    } else {
      this.status = 'all questions completed press submit';
      $('#qdis').hide();
      $('#next').hide();
      $('#submitButton').show();
    }
    this.i += 1;
    console.log('question No=', this.i);
  }
  @action
  previousquestion() {
    //moves to previous question
    this.i -= 1;
    if (this.i >= 0) {
      this.status = '';
      $('#submitButton').hide();
      $('#next').show();
      if (this.i < this.questions.length - 1) {
        var choices, choice;
        choices = document.getElementsByName('qops');
        for (var i = 0; i < choices.length; i++) {
          if (choices[i].checked) {
            choice = choices[i].value;
          }
        }
        this.json = {
          id: String(this.questions[this.i].Question_Id),
          ans_choice: choice,
        };
        this.answered[this.i + 1] = this.json;
        console.log(this.answered);
      } else {
        $('#qdis').show();
      }
      this.Q = this.questions[this.i].Question;
      this.O1 = this.questions[this.i].Option_1;
      this.O2 = this.questions[this.i].Option_2;
      this.O3 = this.questions[this.i].Option_3;
      this.O4 = this.questions[this.i].Option_4;
      $('#op' + this.answered[this.i].ans_choice).prop('checked', true);
      console.log('question number=', this.i + 1);
    } else {
      this.i += 1;
    }
  }
  @action
  submitanswers() {
    //to submit all the answers given by the student.
    console.log('interval', this.interval);
    //this.renderTime(1);
    clearInterval(this.interval);
    var updateMap = {};
    this.answered.forEach((q, i) => (updateMap[i] = JSON.stringify(q)));
    updateMap['metaData'] = JSON.stringify({
      duration: this.duration + '',
      topicId: this.user.getTopicID(),
      loginId: this.user.getloginID(),
    });
    var temp = this;
    console.log('updateMap', updateMap);
    var settings = {
      url: 'http://localhost:8081/Demmoapp/ScoreCalculator',
      method: 'POST',
      timeout: 0,
      data: updateMap,
      error: (err) => {
        console.log('error', err);
      },
      success: (data) => {
        {
          console.log(data);
          temp.score = data;
          this.status = 'exam completed Score:' + temp.score;
          $('#logout').show();
          $('#submit').hide();
          $('#qdis').hide();
          $('#prevNextButtons').hide();
        }
      },
    };

    $.ajax(settings).done(function (response) {
      //console.log(response);
    });
  }
  @action
  goBack() {
    //redirects to student home page
    // this.user.clearAll();
    this.transitionToRoute('studenthome');
  }
}
