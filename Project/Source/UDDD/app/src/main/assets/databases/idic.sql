﻿CREATE TABLE dictionary(table_name TEXT PRIMARY KEY NOT NULL,name TEXT NOT NULL,is_offline INTEGER NOT NULL)WITHOUT ROWID
CREATE TABLE topics(id INTEGER PRIMARY KEY NOT NULL,name TEXT UNIQUE NOT NULL,content TEXT NOT NULL)WITHOUT ROWID
CREATE TABLE words(id INTEGER NOT NULL,topic_id INTEGER REFERENCES topics(id),word TEXT UNIQUE NOT NULL,content TEXT NOT NULL,note TEXT,is_edited INTEGER NOT NULL,picture_url TEXT,PRIMARY KEY(id,topic_id))WITHOUT ROWID
CREATE TABLE progressing(word TEXT NOT NULL,type INTEGER NOT NULL,created_at DATETIME NOT NULL,point INTEGER  NOT NULL,PRIMARY KEY(word,type))WITHOUT ROWID
CREATE TABLE downloaded(word TEXT NOT NULL,content TEXT NOT NULL,table_name TEXT NOT NULL,topic_name TEXT NOT NULL,picture_url TEXT)
CREATE TABLE upload_pending(word TEXT NOT NULL,content TEXT NOT NULL,table_name TEXT NOT NULL,topic_name TEXT NOT NULL,picture_url TEXT)
INSERT INTO dictionary(table_name,name,is_offline)values('words','Từ điển thông dụng',1)
INSERT INTO dictionary(table_name,name,is_offline)values('technology','Từ điển kỹ thuật',0)
INSERT INTO dictionary(table_name,name,is_offline)values('medical','Từ điển y học',0)
INSERT INTO dictionary(table_name,name,is_offline)values('economic','Từ điển kinh tế',0)
INSERT INTO topics(id,name,content)values(0,'','')
INSERT INTO topics(id,name,content)values(1,'Family','Gia đình')
INSERT INTO topics(id,name,content)values(2,'Contracts','Hợp đồng')
INSERT INTO topics(id,name,content)values(3,'Marketing','Tiếp thị')
INSERT INTO topics(id,name,content)values(4,'Warranties','Bảo hiểm')
INSERT INTO topics(id,name,content)values(5,'Business planning','Chiến lược kinh doanh')
INSERT INTO topics(id,name,content)values(6,'Conferences','Hội nghị')
INSERT INTO topics(id,name,content)values(7,'Computers','Máy tính')
INSERT INTO topics(id,name,content)values(8,'Office Technology','Công nghệ văn phòng')
INSERT INTO topics(id,name,content)values(9,'Office Procedures','Qui trình làm việc')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(1,0,'test1',"cA;n;'fæmili;kin:home;;familial;gia đình:this is my family:đây là gia đình của tôi;family man:người có gia đình",'Chung một nhà;Gia đình',0,'http://cdn1.theodysseyonline.com/files/2015/12/07/6358505828960949401519737429_family.jpg')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(2,0,'test2',"cA;n;'fæmili;kin:home;;familial;gia đình:this is my family:đây là gia đình của tôi;family man:người có gia đình",'Chung một nhà;Gia đình',0,'http://cdn1.theodysseyonline.com/files/2015/12/07/6358505828960949401519737429_family.jpg')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(1,1,'family',"cA;n;'fæmili;kin:home;;familial;gia đình:this is my family:đây là gia đình của tôi;family man:người có gia đình",'Chung một nhà;Gia đình',0,'http://cdn1.theodysseyonline.com/files/2015/12/07/6358505828960949401519737429_family.jpg')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(2,1,'father',"A;n;'fɑːðər;dad;descendant;cha:this is my father:đây là cha của tôi;father Christmas:Ông già Noen",'ba;bố',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(3,1,'mother',"A;n;'mʌðə;mom;;mẹ:how are you, Mother ?:mẹ có khỏe không?",'mẹ',0,'http://www.blogjnj.com/wp-content/uploads/2013/05/Mother-and-Daughter.jpg')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(4,1,'grandmother',"A;n;'grændmʌðə;grandma:nana;;bà:this is my grandmother:đây là bà của tôi",'bà nội; bà ngoại',0,'http://www.echocottages.com/wp-content/uploads/2012/07/iStock_000017524811Medium-CROPPEDa.jpg')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(5,1,'grandfather',"A;n;'grændfɑ:ðə;granddad;;ông:this is grandfather:đây là ông của tôi",'Ông nội;ông ngoại',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(1,2,'contract',"AD;n;kən'trækt;agreement:bond;expand:fight off;hợp đồng:sign a contract:kí hợp đồng;đính ước:she had contracted a most unsuitable marriage:cô ta đã có một cuộc đính hôn rất là không xứng đôi;a contract worker:người làm việc theo hợp đồng",'hợp đồng; đính ước; kết giao',0,'http://previews.123rf.com/images/photoman/photoman0906/photoman090600275/5105909-Business-Contract-and-pen-close-up-Stock-Photo-contract-legal-law.jpg')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(2,2,'agreement',"cdA;n;ə'gri:mənt;contract:arrangement;disagreement;agreeable;agree;hợp đồng:please sign the agreement:mời ông ký vào bản hợp đồng;a gentleman's agreement:thỏa thuận danh dự",'hợp đồng;giao kèo',0,'http://rudnermacdonald.com/wp-content/uploads/2015/12/3d_Man_Agreement-300x261.jpg')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(3,2,'assurance',"A;n;ə'∫uərəns;pledge:declaration;timidity;sự chắn chắn, sự đảm bảo::;life assurance:bảo hiểm nhân thọ",'Sự chắc chắn',0,'http://www.lasalle42.fr/wp-content/uploads/2014/05/baisser-frais-assurance-auto-logement.jpg')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(4,2,'determine',"cD;v;di'tə:min;find out;;determined;quyết đinh:hard work determine good results:làm việc tích cực quyết đinh kết quả tốt đẹp;to determine a fate :định đoạt số phận",'quyết định',0,'https://dcbdotme.files.wordpress.com/2014/09/24d_roadsigns-arrows-in-different-directions2-o.png')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(5,2,'cancellation',"cdA;n;kænse'lei∫n;annulment;enactment;canceled;cancel;sự hủy bỉ::",'sự hủy bỏ',0,'http://electrapark.com.au/wp-content/uploads/2014/03/Project-Cancellation-Letter1.jpg')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(6,2,'engage',"D;v;in'geidʒ;involve:occupy;dismiss:repel:disengage;tuyển dụng:to engage a new accountant:tuyển một nhân viên kế toán mới;to engage with something:ăn khớp",'tuyển dụng',0,'http://www.fecon.com.vn/wp-content/uploads/2014/06/tuyendung.jpg')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(7,2,'establish',"D;v;is'tæbli∫;start:creat:begin;disprove;thành lập:to establish diplomatic relations:kiến lập quan hệ ngoại giao",'thành lập',0,'http://www.simplifyem.com/property-management/wp-content/uploads/2011/01/Property-Management-Establish-Maintenance-Repair-System.jpg')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(8,2,'obligate',"acD;v;'ɔbligeit;;;obligation;obligatory;bắt buộc:He felt obligated to help:Nó cảm thấy bị bắt buộc phải giúp đỡ",'bắt buộc; ép buộc',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(9,2,'party',"AA;n;'pɑ:ti;faction:group:cabal;;đảng:a political party:một chính đảng;buổi tiệc:a birthday party:buổi tiệc sinh nhật;the Communist Party: Đảng Cộng Sản",'đảng; bữa tiệc',0,'https://i1.wp.com/www.ttxva.net/wp-content/uploads/2013/07/voilua.jpeg')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(10,2,'provision',"dA;n;prə'viʒn;delivery:facility;;provide;sự cung cấp:the provision of specialist teachers is being increased:đồ cung cấp cho các thầy giáo chuyên nghiệp đang được tăng lên; provision for:sự dự phòng",'sự cung cấp',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(11,2,'resolve',"ADD;n;ri'zɔlv;determination:resolution;indecision;quyết tâm:to take a great resolve to shrink from no difficulty:quyết tâm không lùi bước trước một khó khăn nào;quyết định::;chuyển hóa:water may be resolved into oxygen and hydrogen:nước có thể chuyển hóa thành oxy và hidro",'quyết tâm',0,'http://quyettammanh.vn/wp-content/uploads/2015/12/cach-bo-phim-sex-qt31.jpg')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(12,2,'specific',"CCA;j;spə'sifik;exact:precise;detailed;vague:general:indefinite:generality;rõ ràng:for no specific reason:không có lý do gì rõ ràng;đặc trưng:a style specific to that school of painters:một phong cách đặc trưng cho trường phái hoạ sĩ ấy;cái riêng:moving from the general to the specific:chuyển từ cái chung đến cái riêng;specific weight:trọng lượng riêng",'rõ ràng',0,'http://www.uxd.vn/wp-content/uploads/2015/09/clarity.png')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(1,3,'attract',"acD;v;ə'trækt;draw:mgnetize;repel;attraction;attractive;thu hút:to attract attention:thu hút sự chú ý",'lôi cuốn, thu hút',0,'https://smalltalkbigresults.files.wordpress.com/2012/07/smile-magnet.jpg')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(2,3,'compare',"DA;v;kəm'peə;contrast:evaluate:assess;;so sánh:to compare  (the style of ) the two poems:so sánh (phong cách của) hai bài thơ;sự so sánh::;to compare notes:trao đổi ý kiến",'so sánh',0,'https://anyaworksmart.files.wordpress.com/2013/01/compare-the-incomparable.jpg')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(3,3,'competition',"dcAA;n;,kɔmpi'ti∫n;rivalry:race:struggle;cooperation;complete;completitive;sự cạnh tranh:trade competition between two countries:sự cạnh tranh thương mại giữa hai nước;cuộc thi:beauty competition:cuộc thi hoa hậu;the competition:những người cạnh tranh với ai",'cạnh tranh',0,'http://demosphere.com/wp-content/uploads/2015/07/competitors.jpg')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(4,3,'consume',"acD;v;kən'sju:m;use:spend;conserve;consumer;consumable;tiêu thụ:this engine consumes a ton of coal per hour:máy này tiêu thụ một tấn than một giờ",'tiêu thụ',0,'http://media.tinmoitruong.vn/public/media/media/picture/06/thucpham.jpg')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(5,3,'convince',"D;v;kən'vins;persuade:prove:sway;;thuyết phục::",'thuyết phục',0,'http://www.wikihow.com/images/5/52/Convince-Anyone-of-Anything-Step-2.jpg')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(6,3,'currently',"E;d;'kʌrəntli;now;formerly;hiện nay:our director receives the foreign visitors currently:giám đốc của chúng tôi hiện nay đang tiếp khách nước ngoài",'hiện nay;hiện thời',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(7,3,'fad',"A;n;fæd;fashion:craze:trend:vogue;;mốt nhất thời::",'mốt nhất thời',0,'https://pbs.twimg.com/profile_images/378800000713211877/55d7228c9a2e41925dae54548af6e54c.jpeg')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(8,3,'inspiration',"AA;n;,inspə'rei∫n;stimulus:spur:motivation;disincentive;nguồn cảm hứng:this woman's an inspiration to all of us:người phụ nữ là nguồn cảm hứng cho tất cả chúng ta;sự hít vào::",'nguồn cảm hứng',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(9,3,'market',"AD;n;'mɑ:kit;souk:bazaar:shop;;thị trường:the foreign market:thị trường nước ngoài;bán ở thị trường::;to flood the market:tràn ngập thị trường",'thị trương',0,'http://www.johnharveyphoto.com/HongKong/StreetMarketNearHelensGrandparentsLg.jpg')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(10,3,'persuasion',"A;n;pə'sweiʒn;encouragement:coaxing;;sự tin tưởng:it is my persuasion that:tôi tin chắc là",'sự tin tưởng',0,'http://d3begwarij1g0d.cloudfront.net/wp-content/uploads/2012/10/persuade_and_optimize.jpg')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(11,3,'productive',"C;j;prə'dʌktiv;creative:fecund:prolific;;năng suất:a productive worker:một công nhân có năng suất;productive of something:dẫn đến cái gì",'năng suất',0,'http://www.dumblittleman.com/wp-content/uploads/2014/04/productivity.jpg')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(12,3,'satisfaction',"dcA;n;,sætis'fæk∫n;happiness:joy:pride;dissatisfaction;sự hài lòng:job satisfaction:sự hài lòng về công việc;a look of smug satisfaction :vẻ tự mãn",'sự hài lòng',0,'http://gomerblog.com/wp-content/uploads/2014/06/survey.jpg')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(1,4,'characteristic',"dCA;j;,kæriktə'ristik;typical:distinguishing;uncharacteristic;characterize;tiêu biểu:such insolence is characteristic of him:xấc láo đến vậy là tiêu biểu cho tính cách của hắn;đặc điểm:what characteristics distinguish the Vietnamese from the Chinese:những đặc điểm nào phân biệt người Việt Nam với người Trung Quốc",'tiêu biểu',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(2,4,'consequence',"A;n;'kɔnsikwəns;importance:significance:value;;kết quả:in consequence of something:do kết quả của điều gì;by way of consequences:vì thế",'kết quả',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(3,4,'consider',"acD;v;kən'sidə;respect:count;disregard;consideration;considerable;cân nhắc:We are considering going to  Canada:chúng tôi đang cân nhắc việc đi Canađa;to consider somebody:nghĩ về ai",'cân nhắc',0,'http://ingramswaterandair.com/heat-pump/wp-content/uploads/2011/02/Things-to-consider.jpg')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(4,4,'cover',"AD;n;'kʌvə;jacket:shell:case;;vỏ bọc:under the same cover:trong cùng 1 vỏ bọc;bao bọc:to cover a wall with paper:dán giấy bao bọc tường;to take cover:ẩn núp",'vỏ bọc',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(5,4,'expiration',"A;n;,ekspi'rei∫n;;;sự hết hạn:the expiration of the lease:sự hết hạn hợp đồng thuê nhà",'sự hết hạn',0,'http://www.rogerogreen.com/wp-content/uploads/2016/02/expire-date.jpg')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(6,4,'frequently',"E;d;'fri:kwəntli;often:regularly:normally:commonly:recurrently;infrequently;thường xuyên:He smokes frequently in the computer room:Anh ta thường xuyên hút thuốc trong phòng máy điện toán",'thường xuyên',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(7,4,'imply',"acD;v;im'plai;suggest:infer;;implication;implicit;ngụ ý:silence implies consent:sự im lặng ngụ ý bằng lòng",'ngụ ý;ẩn ý',0,'http://d.f5.photo.zdn.vn/upload/original/2011/03/22/23/39/1300811944618959173_574_0.jpg')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(8,4,'promise',"AD;n;'prɔmis;assurance:undertaking;;lời hứa:we received many promises of help:chúng tôi đã nhận được nhiều lời hứa giúp đỡ;promise of marriage:sự hứa hôn",'lời hứa',0,'http://www.mindstepsinc.com/wp-content/uploads/pinky-promise.jpg')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(9,4,'protect',"acD;v;prə'tekt;defend:guard:keep;neglect;protection;protective;che chở:to protect someone from danger:che chở ai khỏi bị nguy hiểm",'che chở',0,'https://arrajaathehope.files.wordpress.com/2014/03/house-under-protection-yellows.jpg')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(10,4,'reputation',"ccA;n;,repju:'tei∫n;standing:status:repute;;reputable;reputed;sự nổi tiếng:to live up to one's reputation:sống sao cho xứng với danh tiếng của mình",'sự nổi tiếng;danh tiếng',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(11,4,'require',"acDD;v;ri'kwaiə;need:necessitate:want;;requirement;requisite;cần đến:we require extra help:chúng tôi cần được giúp đỡ thêm;yêu cầu:it is required  (of me ) that I give evidence: người ta yêu cầu tôi đưa ra chứng cứ",'cần đến, yêu cầu',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(12,4,'variety',"A;n;və'raiəti;collection:diversity:assortment:selection;;sự đa dạng:a life full of change and variety:một cuộc đời nhiều thay đổi và muôn màu muôn vẻ",'sự đa dạng',0,'http://6iee.com/data/uploads/48/694048.jpg')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(1,5,'address',"AA;n;ə'dres;;;diễn văn::;địa chỉ::",'diễn văn',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(2,5,'avoid',"acD;v;ə'vɔid;evade:circumvent;face;avoidance;avoided;tránh:to avoid smoking:tránh hút thuốc là;to avoid sb like the plague:tránh ai như tránh hủi",'tránh;tránh xa',0,'http://pacificlanworks.com/wp-content/uploads/2010/01/dundo.png')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(3,5,'demonstrate',"acD;v;'demənstreit;explain:expound:display;;demonstration;demonstrative;chứng mính:to demonstrate the truth of a statement:chứng minh sự thật của một lời tuyên bố",'chứng minh',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(4,5,'develop',"aaDD;v;di'veləp;expand:extend;contract;development;developer;phát triển:to develop industry:phát triển công nghiệp;trình bày:to develop one's views on a subject:trình bày quan điểm về một vấn đề",'phát triển;mở rộng',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(5,5,'evaluate',"aaD;v;i'væljueit;assess:appraise:gauge:estimate;;evaluation;evaluator;đánh giá:to evaluate the chances of success of a candidate:đánh giá các cơ hội thành công của một ứng cử viên",'đánh giá;định giá',0,'http://coachfore.org/wp-content/uploads/2012/04/employee-performance-evaluation-form-300x300.jpg')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(6,5,'gather',"DDD;v;'gæðə;meet:collect;disperse;tập hợp:to gather a crowd round:tập hợp một đám đông quanh mình;hái:to gather flowers:hái hoa;hiểu:I can't gather anything from his speech:tôi chẳng hiểu hắn ta nói gì",'tập hợp',0,'http://dailynorthwestern.com/2014/10/02/campus/students-gather-for-vigil-to-mourn-victims-of-the-conflict-in-the-gaza-strip/#prettyPhoto[1]/0/')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(7,5,'offer',"AAD;n;'ɔfə;proposal:suggestion:bid;;lời mời chào:on offer:bán giảm giá;sự trả giá:a firm offer:giá nhất định;biếu:to offer someone something:biếu ai cái gì;to offer a plan:đưa ra một kế hoạch",'lời mời chào',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(8,5,'primarily',"EE;d;'praimərəli;;;trước hết::;chủ yếu::",'trước hết;đầu tiên',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(9,5,'risk',"AAD;n;risk;danger::hazard;;sự rủi ro:at one's own risk:bản thân phải gánh lấy mọi sự rủi ro nguy hiểm;sự mạo hiểm::;mạo hiểm::;to risk one's life:liều mạng",'sự rủi ro',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(10,5,'strategy',"dcA;n;'strætədʒi;plan:scheme:policy:approach;;strategize;strategic;chiến lược:skilled in strategy:giỏi về chiến lược;economic strategies:những chiến lược kinh tế",'chiến lược',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(11,5,'strong',"CC;j;strɔη;powerful:burly:brawny:muscular;;chắc chắn:strong conviction:niềm tin chắc chắn;khỏe:strong health:sức khỏe tốt;strong market:thị trường giá cả lên nhanh",'chắc chắn',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(12,5,'substitution',"dcA;n;,sʌbsti'tju:∫n;replacement:switch:exchange:swap;;sự thay thế:two substitutions were made during the match:trong trận đấu đã thay thế hai cầu thủ",'sự thay thế',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(1,6,'accommodate',"acD;v;əˈkɒmədeɪt;lodge:put up;;accommodation;accommodating;điều chỉnh:I will accommodate my plans to yours:Tôi sẽ điều chỉnh các kế hoạch của tôi theo kế hoạch của anh",'điều chình;điều tiết',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(2,6,'arrangement',"dcA;n;ə'reindʒmənt;preparation:plan:procedure;;sự thu xếpto make one's own arrangements:tự thu xếp;to make arrangements with somebody:dàn xếp với ai",'sự thu xếp',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(3,6,'association',"dcA;n;ə,sousi'ei∫n;organization:union;;associate;associated;đoàn thể::",'đoàn thể',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(4,6,'attend',"aaD;v;ə'tend;;;attendee;attendance;tham dự::",'tham dự;có mặt',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(5,6,'session',"A;n;'se∫n;meeting:sitting:assembly:conference:gathering:hearing;;buổi họp:in session:đang họp;a recording session:buổi ghi âm",'buổi họp',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(6,6,'hold',"DD;v;;;tổ chức:to hold a meeting:tổ chức một cuộc meeting;cầm:to hold a pen:cầm bút",'tổ chức',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(7,6,'location',"A;n;lou'kei∫n;;;vị trí:a suitable location for new houses:vị trí thích hợp cho những nhà mới",'vị trí',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(8,6,'overcrowded',"C;j;,ouvə'kraudid;congested:overloaded:teeming;;đông nghẹt:overcrowded buses:xe buýt đông nghẹt",'đông nghẹt',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(9,6,'register',"aaDA;'redʒistə;enter:list:record:catalog;;register;registration;đăng kí:to register a name:đăng kí tên vào sổ;sổ:register of births:sổ khai sinh",'đăng kí',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(10,6,'select',"acDC;v;si'lekt;choose:pick;;selection;selective;lựa chọn:to select a gift:lựa chọn một tặng phẩm;được lựa chọn::",'lựa chọn',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(1,7,'access',"cAD;n;'ækses;;;accessible;quyền truy cập:you can't gain access to the files unless you know the password:Anh không có được quyền truy cập file trừ phi anh biết mật khẩu;truy cập:to access a file:truy cập một tập tin",'quyền truy cập',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(2,7,'allocate',"acD;v;'æləkeit;assign:allot:apportion;;allocation;allocated;cấp cho::",'cấp cho',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(3,7,'compatible',"C;j;kəm'pætəbl;matching:fitting:consistent;incompatible;tương thích:these dot-matrix printers are compatible with new generation PCs:các máy in kim này tương thích với các loại PC đời mới",'tương thích',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(4,7,'delete',"D;v;di'li:t;remove;insert;xóa::",'xóa',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(5,7,'display',"AD;v;dis'plei;show:exhibit;conceal;sự trưng bày::;hiển thị:to display a dialog box:hiển thị một hộp thoại",'hiển thị',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(6,7,'duplicate',"ACD;v;'dju:plikit;repeat:copy;;bản sao::;sao lại:before you leave, please duplicate that file by making a copy on the CD-ROM:trước khi ngừng làm việc, anh hãy vui lòng nhân bản file đó bằng cách copy từ CD-ROM;gấp đôi::",'bản sao',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(7,7,'failure',"A;n;'feiljə;;;sự hỏng hóc:the repeated failure of the printer baffled the technician:sự hỏng hóc nhiều lần trong việc in ấn của cô ta đã gây trở ngại cho kĩ thuật viên",'hỏng hóc',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(8,7,'ignore',"D;v;ig'nɔ:;overlook:discount;notice;phớt lờ:You've been ignoring me:Anh đã phớt lờ tôi",'phớt lờ',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(9,7,'search',"AD;n;sə:t∫;hunt:exploration;;cuộc tìm kiếm:a search for a missing aircraft:cuộc tìm kiếm chiếc máy bay mất tích;tìm kiếm:to search the house for weapons:khám nhà để tìm kiếm vũ khí;search me:tôi không biết",'tìm kiếm',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(10,7,'warning',"dcA;n;notice:caution:caveat;;warn;warning;lời cảnh báo:he paid no attention to my warnings:nó chẳng để ý tới những lời cảnh báo của tôi",'lời cảnh báo',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(1,8,'affordable',"C;j;ə'fɔ:dəbl;;;có đủ khả năng:the company's first priority was to find an affordable phone system:ưu tiên trước hết của công ty là tìm một hệ thống điện thoại có đủ khả năng",'có đủ khả năng',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(2,8,'capacity',"A;n;kə'pæsiti;volume:size;;sức chứa:a hall with a seating capacity of  2000:phòng lớn có sức chứa 2000 người",'sức chứa;dung tích',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(3,8,'durable',"C;j;'djuərəbl;sturdy:strong;flimsy;bền:durable friendship between two peoples:tình hữu nghị lâu bền giữa hai dân tộc",'bền',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(4,8,'initiative',"dA;n;i'ni∫ətiv;inventiveness:creativity:wits;;intiate;sáng kiến:It is hoped that the government's initiative will bring the strike to an end:Hy vọng rằng sáng kiến của chính phủ sẽ chấm dứt được cuộc bãi công",'sáng tạo',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(5,8,'physically',"E;d;'fizikli;bodily:actually;mentally;về thân thể:to attack someone physically:tấn công ai về thân thể",'về thân thể',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(6,8,'provider',"dA;n;prə'vaidə;breadwinner;;provide;người cung cấp",'người cung cấp',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(7,8,'recur',"acD;v;ri'kə:;relapse:repeat;cease;recurrence;recurring;lặp lại:a recuring problem:một vấn đề cứ lặp lại",'lặp lại',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(8,8,'reduction',"dcA;n;ri'dʌk∫n;discount:decrease:lessening:drop;increase;reduce;reducible;sự giảm bớt:reduction of armaments:sự giảm quân bị",'sự giảm bớt',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(9,8,'stock',"ADC;n;stɔk;supply:stockpile:hoard;;kho:in stock:tồn kho;có sẵn trong kho:stock sizes:các cỡ có sẵn;tích trữ:we do not stock the outsizes:chúng tôi không tích trữ ngoại cỡ",'kho',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(1,9,'appreciation',"dcA;n;ə,pri:∫i'ei∫n;thanks:gratitude;;appreciate;appreciated;sự đánh giá::",'sự đánh giá',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(2,9,'casually',"E;d;'kæʒuəli;carelessly:offhandedly;carefully;không trang trọng:casually dressed:mặc quần áo bình thường",'không trang trọng',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(3,9,'code',"dcA;n;koud;cipher:cryptogram;;code;coded;bộ luật:labour code:bộ luật lao động",'bộ luật',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(4,9,'expose',"acD;v;iks'pouz;uncover:bare;cover;exposure;exposed;vạch trần:to expose crime:vạch trần tội ác",'vạch trần',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(5,9,'glimpse',"AD;n;glimps;look:glance:peek:peep:sight;stare;cái nhìn lướt qua::;to get  (have ) a glimpse of something:nhìn lướt qua cái gì",'nhìn lướt qua',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(6,9,'outdated',"C;j;aut'deitid;antiquated;up-to-date;lỗi thời:outdated clothing:quần áo không còn là mốt nữa",'lỗi thời',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(7,9,'practice',"dcA;n;repetition:rehearsal;;practive;practical;thực hiện:to put a plan into practice:thực hiện kế hoạch",'thực hiện',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(8,9,'reinforce',"aD;v;,ri:in'fɔ:s;strengthen:underpin;weaken;reinforcement;củng cố::",'củng cố;tăng cường',0,'')
INSERT INTO words(id,topic_id,word,content,note,is_edited,picture_url)values(9,9,'verbally',"dcE;d;'və:bəli;orally:vocally;;verbalize;verbal;bằng lời nói::",'bằng lời nói',0,'')