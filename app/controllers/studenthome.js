import Controller from '@ember/controller';
import $ from 'jquery';
import { tracked } from '@glimmer/tracking';
import { action } from '@ember/object';
import { inject as service } from '@ember/service';

export default class StudenthomeController extends Controller {
  @tracked topics = [];
  @tracked history = [];
  @tracked teachers = [];
  @tracked historystatus = '';
  @tracked status = '';
  @tracked votestatus = '';
  @service('userdetails') user;

  //lists all the topics to answer
  @action
  showtopics() {
    console.log('Getting the topics----->');
    // this.examtopics.update(data);
    var temp = this;
    var settings = {
      url: 'http://localhost:8081/Demmoapp/AllTopics',
      method: 'POST',
      timeout: 0,
      data: { loginId: temp.user.getloginID() },
      success: (res) => {
        var data = JSON.parse(res);
        temp.topics.clear();
        temp.topics = data;
        if (data.length == 0) {
          temp.status = 'no exams to display';
        } else {
          temp.status = '';
          $('#examtopics').show();
          $('#newque').hide();
        }
        console.log('Data recieved and displayed');
      },
      error: (err) => alert('error', err),
    };

    $.ajax(settings).done(function (response) {});
  }

  // gets history of all the exams writtem
  @action
  getHistory() {
    var temp = this;
    var settings = {
      url: 'http://localhost:8081/Demmoapp/getHistory',
      method: 'POST',
      timeout: 0,
      data: { loginId: temp.user.getloginID() },
      success: (d) => {
        var data = JSON.parse(d);
        if (data.length == 0) {
          temp.historystatus = 'No history found';
        } else {
          temp.history = data;
          $('#history').show();
        }
      },
    };

    $.ajax(settings).done(function (response) {
      console.log(response);
    });
  }
  // method to get vote details
  getVote(teacher) {
    console.log('teacher=', teacher, 'student=', this.user.getloginID());
    var temp = this;
    var settings = {
      url: 'http://localhost:8081/Demmoapp/TeacherVote',
      method: 'POST',
      timeout: 0,
      data: { Teacher: teacher, loginId: temp.user.getloginID() },
      success: (d) => {
        var data = JSON.parse(d);
        if (data.length == 0) {
          temp.votestatus = 'you can not vote';
          $('#voteButton').hide();
          $('#vote').hide();
        } else if (data['status'] == 'updated') {
          temp.votestatus = 'updated';
          $('#voteButton').hide();
          temp.teachers.clear();
          temp.teachers.addObject(data.teachers);
        } else {
          temp.votestatus = '';
          temp.teachers = data.teachers;
          console.log(temp.teachers);
          if (temp.teachers.length == 1 && data['studentStatus'] == 'voted') {
            $('#voteButton').hide();
            temp.votestatus = 'already voted';
          }
        }
      },
    };

    $.ajax(settings).done(function (response) {
      console.log(response);
    });
  }
  @action
  submitVote() {
    var vote = $('#vote').val();
    console.log('Vote', vote);
    this.getVote(vote);
  }
  @action
  changeVote() {
    // var vote = $('#vote').val();
    // console.log('Vote', vote);
    // this.getVote(vote);
  }
  // redirectto student exam
  @action
  goToTopic(topicID) {
    console.log('redirecting to topicID', topicID);
    this.user.setTopicID(topicID);
    this.transitionToRoute('/studentexam/0');
  }
}
