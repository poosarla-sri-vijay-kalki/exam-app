import Controller from '@ember/controller';
import $ from 'jquery';
import { tracked } from '@glimmer/tracking';
import { action } from '@ember/object';
import { inject as service } from '@ember/service';
export default class TeacherhomeController extends Controller {
  topicName = '';
  des = '';
  @tracked nofailteacher = [];
  @tracked topics = [];
  @tracked noStudentTopic = [];
  @tracked failtopics = [];
  @tracked goodteachers = [];
  @tracked bestTeacher = { uname: '', percent: '' };

  @tracked allTopicStatus = '';
  @tracked NotAnsTopicstatus = '';
  @tracked status = '';
  @tracked bestTeacherStatus = '';
  @tracked metric1 = '';
  @tracked metric2 = '';
  @tracked failTopicstatus = '';

  @tracked fastofall = [];
  @tracked totalSpeed = '';

  @tracked fastoftopics = [];
  @tracked SpeedByTopic = '';

  @tracked fastofteachers = [];
  @tracked SpeedByTeacher = '';

  @tracked teacherbyVotes = [];
  @tracked teacherbyVoteStatus = '';
  @service('userdetails') user;
  @action
  openForm() {
    //to show the form for entering the new topic.
    $('#paperDetails').show();
    $('#addTopicButton').hide();
  }
  @action
  addQuestions() {
    ///redirects to addquestion.hbs
    this.transitionToRoute('addquestion');
  }
  @action
  addNewTopic() {
    //takes the values of t=new topic and saves it.
    var temp = this;
    var regExp = /[a-zA-Z0-9]/;
    var topic = $('#topic').val();
    this.topicName = topic;
    var des = $('#des').val();
    this.des = des;
    var teacher = this.user.getUname();
    var loginId = this.user.getloginID();
    console.log(des);
    console.log('tresult', !regExp.test(topic));
    console.log('desresult', !regExp.test(des));
    if (!regExp.test(topic) || !regExp.test(des)) {
      this.status = 'enther all values';

      return;
    }
    var json = { topic: topic, des: des, teacher: teacher, loginId: loginId };
    console.log(json);
    var settings = {
      url: 'http://localhost:8081/Demmoapp/AddTopic',
      method: 'POST',
      timeout: 0,
      data: json, //{"Topic":"dsh"}
      success: (d) => {
        console.log('FILE started');
        var data = JSON.parse(d);
        if (data.status) {
          temp.status = 'Topic Registered sucessfully';
          $('#paperDetails').hide();
          $('paperDetails').hide();
          $('#addQueButton').show();
          temp.user.addTopic(temp.topicName);
          temp.user.setTopicID(data.topicId);
        } else {
          temp.status = 'Already Topic Registered with Same Name';
          $('paperDetails').trigger('reset');
        }
      },
      error: (_) => {
        alert('ajax error');
        console.log('error');
      },
    };

    $.ajax(settings).done(function (response) {});
  }

  @action
  showTopics() {
    // to show the list of topics published by that teacher
    console.log('getting all topics');
    var data = [];
    var temp = this;
    var loginId = this.user.getloginID();
    console.log(loginId);
    var settings = {
      url: 'http://localhost:8081/Demmoapp/Topicteacher',
      method: 'POST',
      timeout: 0,
      data: { loginId: loginId },
      success: (res) => {
        data = JSON.parse(res);
        temp.topics.clear();
        temp.topics = data;
        console.log(temp.topics);
        if (data.length == 0) {
          temp.allTopicStatus = "You didn't published a topic yet";
          $('#examTopics').hide();
        } else {
          temp.allTopicStatus = '';
          $('#examTopics').show();
        }
      },
    };
    $.ajax(settings).done(function (response) {});
  }
  @action
  showNotAnsTopics() {
    var temp = this;
    var settings = {
      url: 'http://localhost:8081/Demmoapp/ShowNotAnsTopics',
      method: 'POST',
      timeout: 0,
      data: {
        loginId: this.user.getloginID(),
      },
      success: (d) => {
        var data = JSON.parse(d);
        console.log(data);
        temp.noStudentTopic.clear();
        temp.noStudentTopic = data;
        console.log(data);
        if (data.length == 0) {
          temp.NotAnsTopicstatus =
            'there are no topics with no students attempted :)';
        } else {
          temp.NotAnsTopicstatus = '';
        }
      },
    };

    $.ajax(settings).done(function (response) {
      console.log(response);
    });
  }
  @action
  showFailTopics() {
    var temp = this;
    var settings = {
      url: 'http://localhost:8081/Demmoapp/showFailTopics',
      method: 'POST',
      timeout: 0,
      data: { loginId: this.user.getloginID() },
      success: (d) => {
        temp.failtopics.clear();
        var data = JSON.parse(d);
        console.log('hii', data);
        console.log(data.length);
        temp.failtopics = data;
        if (data.length == 0) {
          temp.failTopicstatus =
            'No Topics with less than 50% pass percentage :)';
        } else {
          temp.failTopicstatus = '';
        }
      },
      error: (d) => {
        console.log('error', d);
      },
    };

    $.ajax(settings).done(function (response) {
      console.log(response);
    });
  }
  @action
  allteacherpass() {
    var temp = this;
    var settings = {
      url: 'http://localhost:8081/Demmoapp/teachersAllPass',
      method: 'POST',
      timeout: 0,
      success: (d) => {
        var data = JSON.parse(d);
        temp.goodteachers = data;
        if (data.length == 0) temp.metric1 = 'no best teachers yet!!';
        else {
          temp.metric1 = '';
        }
      },
      error: (d) => {
        console.log(d);
      },
    };

    $.ajax(settings).done(function (response) {
      console.log(response);
    });
  }
  @action
  bestTeache() {
    var temp = this;
    var settings = {
      url: 'http://localhost:8081/Demmoapp/bestTeachers',
      method: 'POST',
      timeout: 0,
      success: (d) => {
        var data = JSON.parse(d);
        temp.bestTeacher = data;
        console.log(data);
        temp.bestTeacher.uname = data.uname;
        temp.bestTeacher.percent = data.PassPercent.toFixed(2) + '';
        if (data.uname == 'no Teacher') {
          temp.bestTeacherStatus = 'no best teacher';
        } else {
          temp.bestTeacherStatus = '';
          $('#table').show();
        }
      },
      error: (d) => {
        console.log(d);
      },
    };

    $.ajax(settings).done(function (response) {
      console.log(response);
    });
  }
  @action
  nofail() {
    var temp = this;
    var settings = {
      url: 'http://localhost:8081/Demmoapp/teacherWithAllPass',
      method: 'POST',
      timeout: 0,
      success: (d) => {
        var data = JSON.parse(d);
        temp.nofailteacher = data;
        if (data.length == 0) {
          temp.metric2 = 'No teachers with no failed students';
        }
      },
      error: (d) => {
        console.log(d);
      },
    };

    $.ajax(settings).done(function (response) {
      console.log(response);
    });
  }
  @action
  goToTopic(topicID) {
    console.log(`From goToTopic --->`, topicID);
    this.user.setTopicID(topicID);
    this.transitionToRoute('/teacherexam/0');
  }
  @action
  deleteTopic(topicID) {
    var temp = this;
    var settings = {
      url: 'http://localhost:8081/Demmoapp/DeleteTopic',
      method: 'POST',
      timeout: 0,
      data: { topicId: topicID },
      success: (_) => {
        console.log('success');
        temp.send('showTopics');
      },
      error: (d) => {
        //console.log(d);
      },
    };

    $.ajax(settings).done(function (response) {
      console.log(response);
    });
  }
  @action
  fastestOfAll() {
    var temp = this;
    var settings = {
      url: 'http://localhost:8081/Demmoapp/totalFastest',
      method: 'POST',
      timeout: 0,
      success: (d) => {
        var data = JSON.parse(d);
        console.log(data);
        if (data.length == 0) {
          temp.totalSpeed = 'No records yet!!';
        } else {
          $('#fastofall').show();
          temp.totalSpeed = '';
          temp.fastofall = data;
        }
      },
    };

    $.ajax(settings).done(function (response) {
      console.log(response);
    });
  }
  @action
  fastestOftopics() {
    var temp = this;
    var settings = {
      url: 'http://localhost:8081/Demmoapp/fastOfTopics',
      method: 'POST',
      timeout: 0,

      success: (d) => {
        var data = JSON.parse(d);
        console.log(data);
        if (data.length == 0) {
          temp.SpeedByTopic = 'No records yet!!';
        } else {
          $('#fastoftopic').show();
          temp.SpeedByTopic = '';
          temp.fastoftopics = data;
        }
      },
    };

    $.ajax(settings).done(function (response) {
      console.log(response);
    });
  }
  @action
  fastestOfTeacher() {
    var temp = this;
    var settings = {
      url: 'http://localhost:8081/Demmoapp/fastOfTeachers',
      method: 'POST',
      timeout: 0,
      success: (d) => {
        var data = JSON.parse(d);
        console.log(data);
        if (data.length == 0) {
          temp.SpeedByTeacher = 'No records yet!!';
        } else {
          $('#fastofteacher').show();
          temp.SpeedByTeacher = '';
          temp.fastofteachers = data;
        }
      },
    };

    $.ajax(settings).done(function (response) {
      console.log(response);
    });
  }
  @action
  bestTeacherbyVotes() {
    var temp = this;
    var settings = {
      url: 'http://localhost:8081/Demmoapp/teacherByVotes',
      method: 'POST',
      timeout: 0,
      success: (d) => {
        var data = JSON.parse(d);
        if (data.length == 0) {
          temp.teacherbyVoteStatus = 'no voting recorded';
          temp.teacherbyVotes.clear();
          $('#bestTeacherbyVotes').hide();
        } else {
          temp.teacherbyVoteStatus = '';
          temp.teacherbyVotes = data;
          console.log('teacher', temp.teacherbyVotes);
        }
      },
    };

    $.ajax(settings).done(function (response) {
      console.log(response);
    });
  }
}
