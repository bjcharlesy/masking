package me.charlesy.masking;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
//import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 脱敏生成唯一姓名（MAP方式脱敏结果唯一）
 * 包括:
 * 一般姓名（单姓（全部为汉字，字符串长度大于等于2小于等于4，第一个汉字按姓氏脱敏，其他汉字按字脱敏））；
 * 少数民族姓名（中间有”·“）（除一般姓名之外的其他）；
 * 英文姓名（大写字母脱敏成大写字母，小写字母脱敏成小写字母，数字脱敏成数字，其他字符保留）
 * 公司名字（汉字脱敏成汉字（公、司、集、团四个字保留），大写字母脱敏成大写字母，小写字母脱敏成小写字母，数字脱敏成数字，其他字符保留）
 * 
 * --通用
 * @author Administrator
 *
 */
public class MODELNAMESKSNMAP {
	
	public  static int[] weighty = { 2, 7, 5, 6, 1, 1, 5, 4, 3, 1, 9, 6, 8, 1, 2, 2, 3, 5 };
	//定义单姓氏数组
	public static final String[] firstnames1={
		"巩", "史", "习", "郎", "苏", "谭", "焦", "萧", "燕", "邢", "卢", "祖", "翟", "樊", "韩", "谷",
		"崔", "赵", "胡", "娄", "谢", "瞿", "符", "皮", "祁", "危", "解", "郁", "卜", "李", "尹", "舒", 
		"弓", "邵", "龙", "盛", "褚", "干", "屈", "雷", "时", "何", "田", "蒋", "支", "闵", "钟", "凌",
		"王", "詹", "杭", "冯", "明", "牧", "高", "陆", "罗", "霍", "金", "云", "阮", "宋", "杜", "贺", 
		"宁", "廖", "石", "鲍", "孙", "巴", "方", "蔡", "窦", "毕", "段", "裘", "胥", "费", "柏", "姚", 
		"康", "庞", "和", "项", "苗", "奚", "湛", "白", "乐", "邹", "伏", "郭", "庾", "万", "季", "常", 
		"施", "阎", "董", "米", "潘", "颜", "秦", "管", "程", "华", "贾", "计", "张", "文", "龚", "童", "君",
		"俞", "吕", "杨", "洪", "束", "严", "柯", "茅", "古", "劳", "范", "殷", "祝", "申", "宫", "付",
		"宣", "路", "卞", "聂", "孟", "向", "余", "喻", "毛", "叶", "乔", "成", "骆", "袁", "包", "马", 
		"廉", "印", "元", "狄", "傅", "齐", "孔", "裴", "曹", "倪", "韦", "隗", "江", "姜", "仇", "纪", 
		"薛", "强", "乌", "富", "景", "禹", "贡", "栾", "熊", "莫", "易", "尤", "鲁", "席", "滕", "邱", 
		"刁", "章", "宗", "蓝", "陶", "山", "荣", "麻", "刘", "经", "戚", "丁", "徐", "伍", "滑", "溥", 
		"艾", "侯", "房", "穆", "戴", "车", "水", "贝", "臧", "周", "黄", "汪", "幸", "葛", "昝", "武", 
		"柴", "陈", "夏", "应", "柳", "梁", "邬", "贲", "甘", "许", "于", "卫", "沈", "林", "扶", "虞", 
		"缪", "邓", "郑", "任", "彭", "梅", "安", "吴", "巫"
	};
	//定义名字数组
	public static final String[] secondnames={
		"照", "水", "岚", "氧", "寸", "茵",
	
		"道", "阳", "码", "娥", "椒", "辫", "涪", "潜", "势", "樊", "钒", "烘", "爵",
		
		"德", "嗜", "春", "破", "焕", "护", "陀", "拘", "攒", "续", "捅", "于", "才",
		
		"哨", "侠", "锚", "吧", "捶", "收", "膛", "凹", "笋", "刻", "渴", "蚕", "子",
		
		"舍", "掠", "适", "芦", "哟", "挞", "幽", "沧", "勒", "授", "颂", "抛",
		
		"皋", "砌", "撬", "御", "栖", "盈", "豁", "抚", "措", "巫", "滑", "侣", "",
		
		"崔", "夹", "钝", "靳", "誓", "刮", "竞", "哼", "葵", "驼", "斌", "浩", "匀",
		
		"无", "空", "觉", "逗", "念", "岔", "拴", "猾", "庭", "凭", "募", "鸦", "奄",
		
		"敬", "链", "容", "基", "骑", "阁", "录", "啦", "擞", "瓜", "恬", "渭", "师",
		
		"棠", "稿", "慕", "啪", "撵", "踊", "厉", "擂", "貉", "裔", "暮", "礁", "换",
		
		"筒", "俩", "沼", "锦", "涩", "耕", "曳", "太", "劲", "巳", "抑", "袱", "瓶",
		
		"梦", "库", "浆", "盾", "轿", "气", "边", "堂", "跑", "孪", "柜", "父", "皮",
		
		"糠", "劳", "教", "仗", "汰", "岭", "踞", "调", "惩", "拌", "搭", "辱",
		
		"咙", "龙", "篇", "桐", "仙", "炭", "赊", "句", "哥", "刘", "柑", "何",
		
		"纶", "烫", "宏", "戏", "吓", "构", "徽", "荤", "捎", "虾", "唆", "茶", "陆",
		
		"伴", "林", "哑", "巡", "八", "涂", "炮", "蜡", "雨", "挤", "杯", "沃", "穷",
		
		"读", "橱", "秃", "傲", "雁", "坊", "狄", "点", "青", "为", "熙", 
		
		"序", "温", "衰", "谦", "笆", "痉", "井", "紊", "乐", "痛", "巷", "靡",
		
		"禽", "俏", "辐", "俭", "多", "泛", "彻", "官", "擅", "银", "淤", "凳", "咆",
		
		"憨", "脆", "闹", "萄", "棚", "皑", "塞", "佃", "蓉", "迄", "惭", "晒", "脾",
		
		"婿", "鸣", "傈", "侨", "僧", "酋", "珐", "拾", "摊", "筐", "浪", "圾", "龟",
		
		"享", "面", "油", "昂", "眠", "航", "嗣", "迁", "生", "绘", "届", "健", "骸",
		
		"坦", "熔", "等", "矾", "起", "社", "日", "衡", "磺", "慧", "校", "奎", "扣",
		
		"义", "蛮", "戴", "显", "辟", "鹃", "亚", "狂", "卿", "琐", "眨", "薯", "招",
		
		"归", "箩", "居", "梭", "课", "舶", "类", "簧", "述", "落", "廖", "聊", "彝",
		
		"粗", "怂", "贺", "热", "酵", "迹", "衫", "晌", "间", "冯", "恋", "盼",
		
		"接", "哉", "悠", "谊", "虞", "恼", "塑", "镍", "耀", "殆", "吹", "态", "瞄",
		
		"襄", "沾", "磊", "钾", "远", "玲", "贫", "拧", "胎", "进", "如", "怠", "将",
		
		"延", "沽", "尖", "荧", "几", "分", "姑", "砷", "巴", "剥", "寨", "榨", "搜",
		
		"楚", "蠕", "式", "嚎", "镁", "差", "独", "纹", "浚", "澄", "讽", "群",
		
		"匠", "懂", "坯", "顿", "铜", "倘", "畜", "沈", "茄", "汤", "筋", "迂",
		
		"万", "体", "映", "歇", "访", "礼", "幅", "侧", "们", "酉", "挺", "想", "加",
		
		"缸", "违", "狱", "喘", "跨", "捕", "侗", "拜", "宵", "迪", "崭",
		
		"控", "陪", "澳", "跳", "牢", "期", "券", "嘛", "牙", "笺", "乖", "孵", "橡",
		
		"垮", "束", "涝", "充", "纤", "抡", "芒", "酮", "驰", "酸", "泵", "摆", "缉",
		
		"蜜", "常", "钳", "胸", "搪", "尔", "钧", "竭", "抿", "毫", "带", "夺",
		
		"情", "坛", "蹿", "堰", "跃", "肤", "冗", "绸", "钥", "埠", "钩", "挡", "榆",
		
		"够", "咕", "镐", "写", "陨", "碎", "恰", "扯", "捞", "馈", "暑", "莫", "倦",
		
		"勾", "靴", "填", "叼", "田", "惋", "甜", "泥", "汞", "瞧", "挝", "淆", "替",
		
		"笛", "内", "曰", "叫", "媚", "机", "截", "瞥", "寻", "约", "吕", "钵", "峨",
		
		"谰", "谜", "狈", "齿", "悯", "衙", "益", "陵", "荐", "惯", "午", "窗", "婶",
		
		"膳", "绎", "眯", "忙", "睬", "姥", "惹", "卫", "儒", "喧", "忧", "苟", "阂",
		
		"垂", "萌", "颅", "补", "娩", "订", "猛", "旺", "肌", "碳", "铀", "凋", "跌",
		
		"某", "咸", "昆", "绍", "汛", "立", "貌", "平", "俐", "莒", "山", "河", "悦",
		
		"磅", "拥", "乍", "蕊", "待", "蹦", "摩", "戍", "掘", "傍", "宇", "流", 
		
		"惶", "申", "郁", "跋", "秋", "翼", "史", "良", "焉", "悸", "侵", "陋",
		
		"境", "曲", "侈", "疙", "慌", "蕾", "馒", "吊", "节", "肖", "羹", "保", "潞",
		
		"澈", "骄", "惧", "断", "天", "陶", "茫", "侯", "服", "蒂", "裸", "混", "尝",
		
		"绦", "烂", "棒", "楷", "舀", "穆", "仅", "逮", "履", "表", "胆", "黍", "嘿",
		
		"纱", "苇", "镑", "吸", "肮", "由", "撂", "滁", "柠", "驳", "穗", "戎", "添",
		
		"黯", "衣", "邱", "扩", "业", "峭", "寿", "绣", "降", "挑", "议", "藻", "津",
		
		"烁", "定", "镜", "也", "稀", "暖", "推", "琅", "腊", "潭", "拱", "洁", "打",
		
		"秀", "障", "移", "稳", "五", "缅", "魏", "耪", "激", "涌", "逾", "旭", "蓖",
		
		"抹", "肆", "奋", "腾", "艘", "曾", "畦", "毡", "再", "蚁", "幸", "姻", "酣",
		
		"辣", "锣", "疚", "宾", "关", "沿", "畔", "储", "涛", "女", "炯", "撩",
		
		"陇", "躲", "庐", "饼", "绒", "孙", "纯", "辉", "米", "匣", "妇", "列", "闻",
		
		"乏", "沁", "隅", "驶", "私", "垛", "凿", "缔", "胞", "安", "逢", "其", "抠",
		
		"曙", "压", "毯", "窝", "偶", "愧", "氢", "詹", "冰", "久", "命",
		
		"锡", "敝", "倔", "耿", "忽", "媒", "刀", "寞", "富", "上",
		
		"涨", "佛", "拷", "诽", "克", "减", "咬", "探", "茬", "坍", "翰", "甚", "剂",
		
		"材", "厌", "您", "松", "垦", "效", "片", "输", "缨", "府", "龚", "插",
		
		"扇", "壶", "葫", "辰", "哭", "然", "皆", "枣", "凸", "碧", "琶", "啮", "哀",
		
		"勉", "乓", "贱", "鞭", "氏", "习", "刷", "铲", "越", "郎", "昌", "掖", "呢",
		
		"耘", "缝", "盐", "胃", "腆", "硫", "层", "见", "背", "各", "蚊", "稽", "宝",
		
		"环", "男", "冻", "兰", "莹", "异", "放", "袒", "染", "聪", "眩", "鳞", "奔",
		
		"循", "救", "肠", "嫂", "艾", "幻", "及", "撑", "胺", "蔚", "韩", "熟", "数",
		
		"得", "膜", "疼", "贷", "嫁", "催", "块", "今", "巩", "铣", "营", "杠", "墒",
		
		"罚", "捐", "芥", "秦", "须", "云", "砂", "利", "诗", "鲤", "长", "璃", "思",
		
		"剁", "翌", "苛", "鹤", "睛", "夫", "麻", "采", "看", "剩", "掩", "伤", "藕",
		
		"桔", "选", "又", "阴", "俗", "亨", "嘶", "隆", "瓷", "赴", "谍", "搞", "埋",
		
		"故", "旋", "殿", "讼", "烷", "试", "妒", "晚", "南", "槛", "娇", "传",
		
		"挫", "短", "塘", "晦", "恒", "都", "星", "俱", "半", "僻", "堑", "圃", "郸",
		
		"础", "丙", "契", "软", "谐", "七", "欢", "挪", "揣", "揩", "驾", "妊", "魁",
		
		"屋", "渠", "休", "琵", "章", "略", "嫡", "孕", "颠", "檀", "痊", "造", "辛",
		
		"诚", "供", "说", "苍", "统", "恨", "炼", "励", "珊", "辽", "企", "掳", "碱",
		
		"审", "鲸", "仍", "讣", "棍", "冀", "囱", "忘", "祁", "寅", "贼", "匹", "蓟",
		
		"丝", "憾", "拣", "暴", "沥", "彩", "回", "格", "脑", "犁", "抽", "留", "徘",
		
		"馋", "鲁", "陛", "诫", "钎", "方", "祈", "厄", "趴", "厢", "逼", "嚣", "吠",
		
		"览", "让", "韧", "比", "栓", "誊", "答", "哎", "队", "旗", "煎", "怜", "怕",
		
		"轮", "尉", "臼", "辜", "恕", "宠", "绿", "郭", "邵", "喊", "斯", "娟", "呀",
		
		"湘", "蘑", "遏", "免", "庙", "搁", "苗", "琴", "汗", "伏", "则", "视", "泪",
		
		"描", "姬", "卡", "鸳", "兴", "弦", "闺", "樟", "诲", "缓", "纪", "暗", "更",
		
		"僳", "靖", "甲", "鄂", "钡", "括", "怀", "钠", "拦", "烽", "硒", "渔", "斋",
		
		"怯", "军", "碴", "频", "蒋", "钨", "寐", "避", "旦", "卯", "揭", "求", "隧",
		
		"辅", "佳", "她", "冉", "咋", "抨", "烤", "阔", "端", "汀", "茅", "向", "属",
		
		"网", "番", "筹", "丛", "了", "寂", "来", "浸", "躺", "康", "翁", "罢", "譬",
		
		"眷", "预", "滴", "盏", "庚", "段", "贵", "膊", "彪", "骏", "帘", "善", "谨",
		
		"汐", "非", "囊", "蓑", "栏", "勇", "狭", "合", "脊", "氛", "轰", "否", "守",
		
		"二", "魔", "动", "壕", "惠", "烟", "渗", "据", "咎", "烙", "壤", "吝", "吐",
		
		"血", "竖", "雏", "毒", "豌", "崩", "跺", "少", "究", "瓮", "雍", "雇", "歼",
		
		"玛", "藏", "诵", "管", "池", "缩", "乱", "含", "仕", "喉", "蝶", "央", "馅",
		
		"洞", "俊", "庞", "沮", "释", "美", "甭", "到", "怨", "吻", "蒲", "廊", "杰",
		
		"酗", "液", "高", "毛", "猖", "暇", "惕", "瑰", "宅", "杆", "地", "萎", "漠",
		
		"赤", "先", "还", "抬", "匡", "扔", "羚", "柬", "溅", "研", "铁", "裹", "铭",
		
		"魄", "菱", "娱", "腔", "琉", "矣", "惊", "淌", "硝", "刨", "捆", "扶", "箱",
		
		"且", "刑", "早", "拓", "幕", "栅", "鼢", "冶", "拢", "毕", "涵", "改", "椅",
		
		"检", "画", "应", "猜", "盆", "狡", "敷", "萨", "妮", "喝", "啥", "鞍", "哪",
		
		"巾", "藩", "项", "建", "缚", "页", "锅", "献", "嫌", "贤", "练", "泊", "引",
		
		"矛", "尘", "解", "持", "裤", "糖", "躁", "署", "板", "近", "记", "雹", "个",
		
		"卷", "迭", "烃", "睹", "副", "搀", "印", "晓", "俯", "牡", "运", "悍", "帽",
		
		"蜂", "廉", "脖", "划", "现", "创", "鼎", "纫", "武", "锄", "景", "涕", "欲",
		
		"螟", "挎", "铅", "焙", "他", "临", "甫", "若", "洪", "弟", "驱", "俞", "渝",
		
		"嘘", "垄", "乞", "祥", "英", "香", "湾", "农", "蜀", "氰", "桶", "遁", "矩",
		
		"盒", "亩", "概", "禁", "坷", "闸", "寥", "坪", "代", "偿", "慷", "但", "硅",
		
		"抢", "刺", "讲", "浦", "妻", "濒", "寇", "途", "筏", "薛", "努", "拈", "摹",
		
		"铬", "荒", "迎", "纳", "喇", "炽", "金", "包", "犀", "广", "惫", "用", "筛",
		
		"豺", "铰", "壬", "损", "圭", "炔", "偏", "乾", "慈", "谢", "躬", "扛", "赠",
		
		"袭", "佑", "耶", "衅", "蘸", "晾", "鉴", "法", "帐", "浇", "炉", "镊", "旁",
		
		"闽", "牟", "胳", "昔", "果", "萤", "沫", "敢", "酥", "弗", "狮", "镀",
		
		"酚", "锑", "户", "侥", "勺", "商", "叉", "江", "维", "绑", "渡", "弘", "泌",
		
		"宪", "擒", "粒", "部", "涉", "奢", "测", "糊", "全", "疫", "咱", "按", "晤",
		
		"寒", "际", "罐", "锋", "翟", "琼", "门", "莆", "锤", "削", "宛",
		
		"剪", "购", "羞", "售", "孝", "戈", "没", "食", "望", "襟", "相", "洗", "芋",
		
		"海", "许", "顷", "京", "欺", "季", "绽", "三", "漫", "洋", "袄", "窍", "岸",
		
		"的", "肃", "藐", "溉", "韦", "渊", "妄", "孤", "棱", "谅", "氨", "悔", "涡",
		
		"驭", "杨", "伙", "喀", "韭", "趣", "华", "凄", "亲", "绊", "炒", "晴", "草",
		
		"昧", "粕", "梅", "埂", "啼", "随", "幼", "帖", "拆", "禄", "哈", "帅", "砾",
		
		"斜", "腹", "癸", "掀", "剧", "迟", "碾", "荫", "禹", "扫", "邑", "寡", "母",
		
		"醛", "狠", "捌", "昭", "兑", "矽", "跟", "难", "聘", "卉", "漳", "咽", "挛",
		
		"歧", "陌", "塔", "鹅", "港", "赫", "畴", "仑", "绰", "帕", "径", "彭", "菏",
		
		"荔", "勘", "栗", "牵", "灿", "房", "样", "齐", "撤", "椽", "撅", "翱", "言",
		
		"醋", "蔓", "峡", "芭", "碉", "冲", "腥", "爱", "阶", "脱",
		
		"屯", "艰", "浴", "蹲", "聂", "簇", "愉", "依", "虱", "僚", "乎", "予",
		
		"滥", "棋", "笔", "蔫", "拍", "奉", "卤", "氖", "窄", "颐", "掣", "介",
		
		"乃", "诉", "醚", "摈", "色", "瘴", "箍", "麓", "踏", "闭", "薪", "炸", "凤",
		
		"辊", "掐", "对", "逛", "框", "屿", "僵", "颖", "凯", "洱", "崎", "握", "愚",
		
		"典", "从", "圆", "漆", "亮", "能", "译", "涧", "篙", "妹", "裂", "呐", "风",
		
		"攻", "符", "舵", "耐", "蹬", "搅", "蛆", "磨", "彤", "友", "戮", "攘", "淮",
		
		"伞", "磐", "初", "兽", "泉", "任", "侩", "办", "隐", "沛", "膝", "过", "讫",
		
		"瞪", "嘲", "乘", "砰", "饭", "柳", "次", "语", "姜", "苞", "膨",
		
		"廓", "苯", "蕴", "弥", "线", "型", "矢", "忆", "派", "翻", "靠", "纬", "领",
		
		"斧", "讶", "邓", "懒", "配", "大", "悄", "低", "启", "室", "哦", "黔", "裙",
		
		"韶", "倍", "新", "颈", "垢", "藤", "拿", "箭", "蛾", "叭", "窃", "陈", "零",
		
		"杂", "舒", "吨", "烹", "静", "慨", "晨", "馏", "升", "锐", "吮", "篮", "喂",
		
		"根", "弃",  "费", "旬", "眶", "首", "湍", "厨", "优", "燃", "忻",
		
		"蔼", "历", "剑", "蜕", "阎", "财", "岁", "恿", "允", "行", "畸", "芯", "拐",
		
		"捻", "模", "捷", "除", "篱", "蔽", "亏", "匙", "骋", "粮", "信", "豹", "厩",
		
		"庶", "柄", "厦", "唉", "唐", "千", "增", "揽", "颜", "臆", "凛", "腺", "锹",
		
		"名", "柏", "析", "睡", "明", "尚", "癣", "艳", "化", "瑟", "防", "斩", "园",
		
		"鲜", "砒", "算", "具", "凉", "绞", "奈", "下", "晶", "强", "娄", "互", "掇",
		
		"考", "扁", "耽", "汕", "黑", "设", "蚌", "酬", "淡", "头", "镰", "熄", "象",
		
		"霜", "辩", "疮", "夜", "遇", "爹", "叹", "酞", "麦", "悲", "简", "褐", "吉",
		
		"梁", "锁", "味", "西", "觅", "戌", "糜", "焦", "咏", "讳", "丰", "疗", "沉",
		
		"败", "惦", "例", "袜", "揉", "膀", "漾", "戒", "蒙", "账", "逞", "伟", "拨",
		
		"葛", "树", "俘", "负", "假", "喷", "轨", "涯", "询", "衔", "沤", "锯", "童",
		
		"猩", "枢", "楞", "魂", "题", "恢", "身", "卧", "刁", "两", "稠", "酱", "泅",
		
		"蓬", "婚", "宣", "挥", "虹", "滩", "淋", "蹈", "噶", "精", "灵", "忍", "峻",
		
		"赔", "扦", "通", "莎", "急", "冬", "条", "浅", "绚", "停", "后", "衬", "缎",
		
		"谬", "佣", "芬", "谈", "药", "碗", "呆", "绪", "投", "协", "宫", "围", "丹",
		
		"贯", "朔", "茹", "仁", "瞻", "盎", "铂", "轻", "凡", "玩", "沦", "岿", "成",
		
		"姐", "亥", "侍", "枚", "般", "役", "柴", "柯", "榜", "餐", "宿", "牲", "勋",
		
		"衍", "骡", "反", "严", "交", "函", "晋", "蜒", "抖", "鄙", "杭", "赦", "腑",
		
		"卸", "臣", "惑", "必", "沙", "育", "朋", "柿", "东", "北", "梗", "屈", "扎",
		
		"买", "疾", "赐", "却", "峰", "屏", "撒", "奴", "绵", "粟", "踢", "困", "汪",
		
		"坚", "薄", "荷", "胶", "枫", "提", "虏", "钦", "赣", "讥", "篓", "册", "蒜",
		
		"绷", "肝", "双", "喜", "胁", "羊", "梳", "哗", "靶", "篷", "验", "盔", "斗",
		
		"朗", "缆", "歌", "姨", "卞", "举", "氯", "煌", "砚", "岛", "娠", "捍", "帝",
		
		"墨", "胜", "阜", "竟", "涅", "尼", "手", "载", "蛙", "款", "赢", "右", "督",
		
		"呈", "壳", "请", "步", "另", "奠", "丁", "话", "孟", "懊", "使", "问", "兼",
		
		"遗", "夏", "闯", "口", "森", "湃", "垒", "劈", "仓", "士", "扳", "入", "羔",
		
		"害", "苫", "借", "蛤", "溃", "尽", "橇", "顺", "呜", "百", "尾", "率",
		
		"战", "燎", "夕", "胖", "黎", "刚", "颁", "婴", "渣", "汇", "而", "亦", "巨",
		
		"鹰", "像", "呛", "弄", "吴", "人", "获", "绝", "爽", "丢", "报", "榴",
		
		"践", "幌", "埃", "茸", "火", "誉", "牺", "察", "奥", "淀", "瑚", "秸",
		
		"咯", "图", "罕", "滔", "宁", "呵", "吞", "泞", "锻", "论", "疆", "眉", "微",
		
		"匝", "策", "镣", "灯", "音", "曝", "需", "川", "瀑", "摔", "饶", "实", "讹",
		
		"蹭", "丈", "鸡", "嗽", "系", "涤", "锭", "坎", "未", "末", "脸", "堕", "漱",
		
		"额", "粉", "存", "险", "箔", "影", "儿", "串", "牛", "累", "离", "谭", "糯",
		
		"撕", "拎", "徊", "踩", "蝎", "飘", "霄", "焰", "邮", "赵", "蓝", "遭", "编",
		
		"莱", "鞠", "掌", "溯", "筷", "恍", "瓤", "殃", "散", "浓", "婆",
		
		"感", "娜", "蔑", "梢", "撮", "诺", "忿", "满", "在", "烧", "捏", "吼", "郧",
		
		"劫", "狞", "蛇", "拒", "姚", "拟", "仪", "痴", "干", "倡", "光", "枯", "辆",
		
		"姆", "裕", "始", "竿", "遍", "贝", "泳", "霉", "嵌", "萍", "核", "况", "壁",
		
		"络", "汝", "溜", "退", "形", "粘", "潮", "炊", "绥", "沂", "胰", "时", "马",
		
		"洽", "彬", "扭", "搬", "蚀", "仿", "瘩", "糙", "识", "泄", "茎", "榷", "携",
		
		"拂", "俄", "复", "鸥", "斤", "愁", "谗", "辖", "以", "酶", "弹", "沸", "哆",
		
		"棘", "荆", "啸", "连", "窑", "敌", "隔", "阮", "恭", "里", "恩", "布", "毁",
		
		"熏", "哩", "倚", "耳", "盟", "趁", "溪", "械", "唾", "失", "翘", "顽", "叠",
		
		"雾", "贬", "磁", "瑞", "惜", "捧", "套", "顶", "梨", "杜", "秤", "扼", "碰",
		
		"矫", "琳", "翅", "豫", "目", "幂", "妥", "绢", "肋", "秘", "详",
		
		"碑", "距", "孰", "幢", "豆", "芍", "曹", "邀", "吏", "泼", "掸", "饯", "赡",
		
		"唱", "白", "蔡", "拖", "披", "判", "角", "澡", "雄", "窜", "埔", "隶", "冠",
		
		"椭", "碍", "量", "榔", "恐", "啡", "艇", "返", "缄", "曼", "评", "邻", "闪",
		
		"福", "菜", "湛", "托", "潘", "土", "辨", "饺", "耻", "取", "淖", "诧", "民",
		
		"盯", "粤", "砸", "稗", "伐", "怪", "什", "菩", "咀", "吾", "客", "帛", "术",
		
		"炳", "恶", "吃", "底", "猴", "韵", "吩", "窥", "峪", "勿", "笼", "矮", "谣",
		
		"郴", "攀", "诣", "挖", "澜", "甥", "泣", "杏", "四", "决", "潍", "荚", "肄",
		
		"殴", "示", "窖", "砍", "送", "伪", "裳", "袖", "纽", "似", "屹", "党", "弱",
		
		"船", "城", "葡", "已", "给", "羡", "六", "错", "醇", "叙", "贴", "胡", "滦",
		
		"亿", "袋", "赁", "刃", "切", "坝", "歉", "虎", "济", "谷", "褪", "亭", "扰",
		
		"兄", "台", "豢", "你", "确", "责", "匿", "孺", "戚", "唤", "闷", "疥", "物",
		
		"盂", "当", "萝", "颤", "悼", "掂", "危", "查", "瞒", "产", "隋", "洼", "坟",
		
		"学", "舱", "刹", "隙", "揪", "禾", "阅", "畅", "絮", "拼", "鼓", "缘", "佰",
		
		"叶", "不", "素", "键", "宜", "殷", "胯", "朽", "绳", "遂", "滚", "乔", "把",
		
		"膏", "揖", "瞬", "叔", "与", "秒", "去", "涟", "慑", "颧", "董", "兔", "瞅",
		
		"敏", "搽", "靛", "普", "铺", "鸯", "凝", "叮", "同", "债", "恤", "闰", "外",
		
		"坞", "木", "共", "蹋", "塌", "敛", "霸", "慰", "嗓", "巢", "穴", "迅",
		
		"野", "隘", "播", "钞", "诱", "圈", "鹿", "弧", "桥", "佯", "锈", "梯", "张",
		
		"清", "渤", "疏", "听", "计", "扑", "敖", "逸", "驹", "度", "稍", "晕", "意",
		
		"秧", "豪", "丽", "皇", "刊", "燕", "牧", "阐", "吟", "签", "娶", "巍", "驮",
		
		"雕", "虚", "沪", "厅", "源", "被", "排", "陷", "镭", "殊", "净", "笑", "铝",
		
		"夸", "娘", "乳", "岂", "堆", "馆", "花", "洛", "裴", "溺", "撼", "卢", "迸",
		
		"销", "缺", "余", "睦", "弯", "辈", "勤", "佩", "吭", "疑", "拳", "虽", "醒",
		
		"触", "丑", "蛋", "炬", "颗", "茂", "葱", "嘎", "俺", "墟", "谓", "穿", "纺",
		
		"卜", "孔", "古", "鸿", "汽", "瘦", "力", "洒", "茨", "缠", "批", "楔", "妙",
		
		"泽", "露", "赌", "活", "谱", "屠", "惟", "汉", "匆", "本", "雀", "碟", "婉",
		
		"祷", "篡", "货", "懈", "书", "臂", "伶", "苦", "鼻", "祸", "估", "媳", "抒",
		
		"司", "堵", "脉", "菌", "橙", "檬", "饱", "舆", "烬", "择", "鹏", "养",
		
		"它", "淑", "垃", "钢",  "寺", "莽", "永", "莲", "柔", "岩", "睫",
		
		"删", "旷", "特", "裁", "希", "钱", "卖", "冈", "堤", "饿", "每",
		
		"札", "达", "芹", "邦", "桅", "并", "氓", "恳", "慢", "擦", "误", "谴", "饵",
		
		"秉", "霓", "肩", "赞", "丫", "谁", "酷", "噬", "囤", "标", "嘻", "锌", "歹",
		
		"铆", "寝", "垣", "艺", "屉", "奇", "黪", "萧", "肥", "声", "伊", "饰", "盖",
		
		"很", "熬", "好", "苏", "坡", "嫩", "卵", "惰", "擎", "是", "细", "掏", "朴",
		
		"跪", "溶", "驴", "宴", "呻", "舞", "堪", "墅", "悬", "促", "乙", "痕",
		
		"波", "锰", "搓", "伸", "绅", "超", "要", "馁", "柒", "蛹", "梧", "菲", "虐",
		
		"啤", "抄", "湖", "涸", "滤", "务", "尤", "谩", "剃", "赋", "墓", "家", "趟",
		
		"樱", "年", "床", "晃", "剖", "傣", "芳", "酝", "淳", "告", "密", "铡", "参",
		
		"狐", "缕", "勃", "烦", "月", "毋", "钙", "恃", "霹", "币", "酪", "缴", "氦",
		
		"备", "尺", "赏", "扬", "掉", "猿", "律", "墩", "灌", "搂", "芽", "蕉", "谋",
		
		"透", "犊", "煽", "壹", "别", "桃", "枉", "卑", "漂", "霍", "聚", "镶", "亢",
		
		"胚", "霖", "翠", "盛", "玻", "撇", "龄", "驯", "球", "祭", "愿", "呸", "文",
		
		"一", "登", "摧", "经", "称", "滇", "庸", "雌", "稻", "墙", "盘", "劝", "愤",
		
		"贩", "振", "辞", "滨", "邯", "盗", "硬", "较", "傅", "佬", "深", "斑", "鹊",
		
		"仆", "蓄", "逆", "髓", "糕", "十", "出", "料", "寄", "躯", "兵", "兢", "那",
		
		"搏", "猎", "粱", "斥", "巧", "缮", "贡", "摘", "轩", "槽", "枷", "竣", "诡",
		
		"娃", "桓", "粳", "联", "孩", "顾", "耙", "迢", "苹", "栽", "挂", "拇", "票",
		
		"旅", "莉", "雷", "栋", "祟", "懦", "棵", "椰", "蚂", "憎", "皖", "菇", "前",
		
		"脯", "呕", "硷", "辗", "淬", "唬", "汲", "讯", "电", "凰", "傀", "芜", "训",
		
		"性", "刽", "观", "犹", "雪", "涎", "沟", "骇", "息", "钉", "羽", "袁", "皂",
		
		"腿", "欠", "煤", "欧", "挠", "继", "稼", "辑", "培", "茧", "贰", "嗡", "席",
		
		"炎", "绕", "即", "咐", "域", "威", "扒", "窟", "车", "颇", "腰", "倾", "忱",
		
		"苑", "雅", "眼", "涣", "悟", "颊", "桨", "桑", "逻", "么", "宦", "轧",
		
		"戊", "铱", "磷", "夯", "诀", "结", "快", "原", "岳", "仟", "匈", "和", "浑",
		
		"范", "赃", "覆", "游", "钮", "恫", "积", "碌", "戳", "郡", "舜", "讨", "词",
		
		"阿", "迷", "浮", "品", "伦", "彰", "甩", "耸", "权", "庆", "鲍", "崇", "呼",
		
		"垫", "敞", "迫", "功", "博", "贪", "乌", "案", "腮", "窿", "默", "可", "碘",
		
		"肺", "击", "灰", "愈", "监", "哮", "宽", "慎", "瓣", "磋", "援", "踌",
		
		"笨", "捡", "槐", "些", "玖", "伎", "悉", "老", "狸", "燥", "索", "挽", "找",
		
		"第", "惺", "拉", "哺", "奖", "承", "鸵", "籍", "演", "陡", "舌", "摸", "眺",
		
		"店", "疲", "囚", "润", "耍", "虑", "纷", "趋", "甘", "候", "毗", "鸽",
		
		"崖", "腕", "杉", "罗", "响", "徐", "冷", "废", "寓", "扮", "唯", "饮", "乒",
		
		"褂", "菠", "牌", "肯", "夷", "锨", "棉", "粹", "盲", "怖", "封", "瓢", "修",
		
		"神", "瞳", "妖", "冕", "朝", "畏", "喻", "玫", "抉", "蝉", "或", "绩", "郊",
		
		"令", "廷", "菊", "理", "肚", "往", "圣", "导", "岗", "酒", "澎", "班", "皿",
		
		"小", "因", "嚷", "沏", "税", "九", "就", "敦", "屡", "抵", "熊", "彦", "占",
		
		"淘", "赎", "峦", "极", "箕", "射", "剔", "汾", "毖", "厘", "突", 
		
		"彼", "唇", "旱", "捂", "帆", "赛", "尧", "晰", "施", "怒", "邢", "檄", "叁",
		
		"朵", "架", "硕", "器", "骆", "件", "敲", "毅", "荣", "抱", "徒", "堡", "君",
		
		"嘉", "瑶", "王", "藉", "赖", "酿", "怎", "闲", "弛", "冒", "警", "繁", "漓",
		
		"坏", "毙", "认", "鞋", "位", "便", "簿", "挟", "宰", "倒", "忌", "倪", "渺",
		
		"瓦", "既", "紧", "纠", "妨", "旧", "遣", "均", "枪", "羌", "丘", "泡", "迈",
		
		"蟹", "价", "腻", "兜", "耗", "石", "氟", "遥", "受", "摄", "飞", "谚", "喳",
		
		"阑", "暂", "躇", "侮", "铃", "惮", "摇", "版", "凌", "付", "爆", "舷", "钓",
		
		"融", "翔", "氮", "仰", "狙", "玄", "厚", "斡", "昏", "欣", "规", "焚", "凑",
		
		"担", "泰", "黄", "抗", "逊", "玉", "变", "伯", "掺", "陕", "烈", "革", "梆",
		
		"灶", "吁", "押", "级", "姓", "此", "剿", "袍", "捣", "狼", "饥", "完", "郝",
		
		"档", "渐", "拭", "红", "固", "我", "递", "蔬", "纲", "褒", "蔷", "附", "咖",
		
		"炕", "歪", "灾", "李", "世", "秆", "焊", "阀", "矿", "苔", "速", "磕", "消",
		
		"坤", "偷", "屑", "丸", "横", "桂", "灸", "贾", "尹", "爬", "该", "鞘", "螺",
		
		"鱼", "事", "诞", "釜", "椿", "辕", "宋", "湿", "伍", "场", "界", "帮", "栈",
		
		"己", "霞", "挨", "杖", "弓", "拔", "舰", "赶", "甸", "溢"
	};
	//定义大写英文字母数组
	public static final String[] englishsb={
		"B", "Z", "V", "D", "Q", "E", "K", "P", "M", "J", "H", "U", "X", "R", "T", "Y",
		"F", "N", "G", "C", "W", "S", "L", "O", "A", "I"
	};

	//定义小写英文字母数组
	public static final String[] englishss={
		"b", "z", "v", "d", "q", "e", "k", "p", "m", "j", "h", "u", "x", "r", "t", "y",
		"f", "n", "g", "c", "w", "s", "l", "o", "a", "i"
	};
	//数字数组
	public static final String[] Number={
		"2", "9", "7", "3", "0", "8", "6", "4", "5", "1"
	};
	//定义不变的汉字单元
	public static final String[] ExceptChar={
			"有","限","公","司","集","团"
	};
	//定义单姓氏泛型
	public  static HashMap<String, String> XingcodeMap = new HashMap<String, String>();
	//定义名字泛型 
	public  static HashMap<String, String> MingcodeMap = new HashMap<String, String>();
	//定义大写英文字母泛型 
	public  static HashMap<String, String> EBcodeMap = new HashMap<String, String>();
	//定义小写英文字母泛型 
	public  static HashMap<String, String> EScodeMap = new HashMap<String, String>();
	//定义数字泛型 
	public  static HashMap<String, String> NcodeMap = new HashMap<String, String>();
	//定义不变的汉字单元泛型
	public  static HashSet<String> ExceptCharSet = new HashSet<String>();
	//判断空格正则表达式
	public  static String regEx1 = "^[\\u0020\\u3000]*$";
	//判断汉字正则表达式
	public  static String regEx = "^[\u4e00-\u9fa5]*$"; 
//	public  static Pattern pat = Pattern.compile(regEx); 
	//判断大写字母正则表达式
	static String SBGregEx = "[A-Z]";
	//判断小写字母正则表达式
	static String SSGregEx = "[a-z]";
	//判断数字正则表达式
	static String NGregEx = "[0-9]";
	private int addnumber = 1;
	private String secretkey16 = "1";
	public MODELNAMESKSNMAP(){
//		init();
	}
	
	private void init(){
		int exl = ExceptChar.length;
		int f1l = firstnames1.length;
		int ssl = secondnames.length;
		int ebl = englishsb.length;
		int esl = englishss.length;
		int nbl = Number.length;
		for(int i=0;i<exl;i++){
			ExceptCharSet.add(ExceptChar[i]);
		}
		for(int j=0;j<f1l;j++){
			XingcodeMap.put(firstnames1[j], firstnames1[(j+addnumber)%firstnames1.length]);
		}
		for(int l=0;l<ssl;l++){
			MingcodeMap.put(secondnames[l], secondnames[(l+addnumber)%secondnames.length]);
		}
		for(int m=0;m<ebl;m++){
			EBcodeMap.put(englishsb[m], englishsb[(m+addnumber)%englishsb.length]);
		}
		for(int n=0;n<esl;n++){
			EScodeMap.put(englishss[n], englishss[(n+addnumber)%englishss.length]);
		}
		for(int o=0;o<nbl;o++){
			NcodeMap.put(Number[o], Number[(o+addnumber)%Number.length]);
		}
	}
	public Date decrypt(Date paramDate)throws Exception{
		Date paramData = paramDate;
		return paramData;
	};

	public long decrypt(long paramLong)throws Exception{
	    long paramData = paramLong;
	    return paramData;
	};

	public String decrypt(String paramString)throws Exception{
		String paramData = paramString;
		return paramData;
	};

	public Date encrypt(Date paramDate)throws Exception{
		Date paramData = paramDate ;
		return paramData;
	};

	public long encrypt(long paramLong)throws Exception{
		String datas = String.valueOf(paramLong);
		try{
			datas = encrypt(datas);
			paramLong = Long.valueOf(datas);
		}catch(Exception e){
			return paramLong;
		}
		return paramLong;
	};
	
	/**
	 * 保留字段两边空格
	 * @param paramStrings
	 * @return
	 */
//	public String[] encrypt_s(String paramStrings){
//		int psl = paramStrings.length();
//		String paramString = paramStrings.trim();
//		int pl = paramString.length();
//		String[] es = new String[3];
//		es[0] = "";
//		es[1] = "";
//		es[2] = "";
//		if(psl==pl){
//			es[1] = paramString;
//		}else{
//			int j=0;
//			for(;j<psl;j++){
//				String psls = paramStrings.substring(j,j+1);
//				if(!psls.matches("[\\u0020\\u3000]")){
//					break;
//				}
//			}
//			if(j>0){
//				for(int k=0;k<j;k++){
//					es[0] = es[0] + " ";
//				}
//			}
//			es[1] = paramString;
//			if((psl-pl)>j){
//				int m = psl-pl-j;
//				for(int n=0;n<m;n++){
//					es[2] = es[2] + " ";
//				}
//			}
//		}
//		return es;
//	}
	/**
	 * 脱敏唯一仿真姓名
	 */
	public  String encrypt(String paramStrings)throws Exception{

		//如果脱敏字符串去掉空格后为空，返回空格
		if( paramStrings ==null || "".equals(paramStrings) || paramStrings.matches(regEx1)){
			return paramStrings;
		}
		if(ExceptCharSet.size()==0){
			init();
		}
		String paramString = paramStrings.trim();
		int pl = paramString.length();
		//定义生成的新名字
		StringBuilder paramData = new StringBuilder();
		String str;
		String value;
		try{
			if(paramString.matches(regEx)&&pl>=2&&pl<=4){//如果脱敏字符串全部为汉字且字符串长度大于等于2小于等于4
				str = paramString.substring(0,1);
				value = XingcodeMap.get(str);//取得单姓
				if(value!=null&&!"".equals(value)){
					paramData.append(value);
				}else{
					paramData.append(str);
				}
				for(int i=1;i<pl;i++){
					str = paramString.substring(i,i+1);
					if(!ExceptCharSet.contains(str)){
						value = MingcodeMap.get(str);//取得名字
						if(value!=null&&!"".equals(value)){
							paramData.append(value);
						}else{
							paramData.append(str);
						}
					}else{
						paramData.append(str);
					}
				}
			}else{
				//循环脱敏字符串
				int chr = 0;
				for(int i=0;i<pl;i++){
					str = paramString.substring(i, i+1);
					chr = str.charAt(0);
					//如果字符为汉字
					if(str.matches(regEx)){
						if(!ExceptCharSet.contains(str)){
							value = MingcodeMap.get(str);//取得名字
							if(value!=null&&!"".equals(value)){
								paramData.append(value);
							}else{
								paramData.append(str);
							}
						}else{
							paramData.append(str);
						}
					}else if(chr>=65&&chr<=90){//如果为大写字母
						value = EBcodeMap.get(str);
						if(value!=null&&!"".equals(value)){
							paramData.append(value);
						}else{
							paramData.append(str);
						}
					}else if(chr>=97&&chr<=122){//如果为小写字母
						value = EScodeMap.get(str);
						if(value!=null&&!"".equals(value)){
							paramData.append(value);
						}else{
							paramData.append(str);
						}
					}else if(chr>=48&&chr<=57){//如果为数字
						value = NcodeMap.get(str);
						if(value!=null&&!"".equals(value)){
							paramData.append(value);
						}else{
							paramData.append(str);
						}
					}else {//其他字符，直接使用，不做脱敏
						paramData.append(str);
					}
				}
			}
			
		} catch(Exception e){
			return paramString;
		}
		//返回值长度大于脱敏值长度时，截取脱敏值长度的字符串
		int pdl = paramData.length();
		if(pdl>pl){
			return paramData.toString().substring(0, paramString.length());
		}
		return paramData.toString();
	}
	public void setParam(Object paramObject){
		try{
			HashMap obj = (HashMap)paramObject;
			if(obj!=null){
				if(obj.get("SECRETKEY16")!=null){
					secretkey16 = (String)obj.get("SECRETKEY16");			
				}
			}
			if(countParam(secretkey16,weighty)!=null){
				this.addnumber = Integer.valueOf(countParam(secretkey16,weighty));
			}
			init();
		}catch(Exception e){
			this.addnumber = 1;
			init();
		}
	}
	
	public String countParam(String strings, int[] weighty) {
		if (null == strings || weighty == null) {
			return null;
		}
		
		int len = strings.length();
		int[] weight = new int[len];

		if(weighty.length<len){
			for(int m=0;m<weighty.length;m++){
				weight[m] = weighty[m];
			}
			int k=1;
			for(int n=weighty.length;n<len;n++){
				weight[n] = k;
				k++;
			}
		}else{
			for(int m=0;m<len;m++){
				weight[m] = weighty[m];
			}
		}
		
		char[] strary = strings.toCharArray();
		int[] intstr = new int[len];
		for (int i = 0; i < len; i++) {
			if (strary[i] <= '9' && strary[i] >= '0') {
				intstr[i] = strary[i] - 48;
			} else if (strary[i] >= 'A' && strary[i] <= 'Z') {
				intstr[i] = strary[i] - 55;
			} else if (strary[i] >= 'a' && strary[i] <= 'z') {
				intstr[i] = strary[i] - 87;
			} else {
				return null;
			}
		}
		int cc, dn;
		int sb = 0 ;

		int e = 0;
		for (int i = 0; i < len; i++) {
			cc = (weight[i] * intstr[i]) % 100;
			dn = (cc / 10 + cc % 10) % 11;
			dn = (dn == 10 ? 0 : dn);
			e += dn;
		}
		sb = e;
		
		return String.valueOf(sb);
	}
	public static void main(String []args)throws Exception{
		int count = 1;
		boolean isPrint = true;
//		for(int i=0;i<count;i++){
//			MODELNAMESMAP cs = new MODELNAMESMAP();
//			String css = cs.encrypt("V型开关OPEN");
//			if(isPrint){
//				System.out.println(css);
//			}
//		}
//		for(int i=0;i<count;i++){
//			MODELNAMESMAP cs = new MODELNAMESMAP();
//			String css = cs.encrypt("　　　　　");
//			if(isPrint){
//				System.out.println(css);
//			}
//		}
//		for(int i=0;i<count;i++){
//			MODELNAMESMAP cs = new MODELNAMESMAP();
//			String css = cs.encrypt("          ");
//			if(isPrint){
//				System.out.println(css);
//			}
//		}
		for(int i=0;i<count;i++){
			MODELNAMESKSNMAP cs = new MODELNAMESKSNMAP();
			String css = "  华夏asdf345435  ";
			if(isPrint){
				System.out.println(css.matches("^(?=.*((公司)|(集团))).*$"));
			}
		}
//		for(int i=0;i<count;i++){
//			MODELNAMESMAP cs = new MODELNAMESMAP();
//			String css = cs.encrypt("张华集团");
//			if(isPrint){
//				System.out.println(css);
//			}
//		}
//		for(int i=0;i<count;i++){
//			MODELNAMESMAP cs = new MODELNAMESMAP();
//			String css = cs.encrypt("嘻嘻");
//			if(isPrint){
//				System.out.println(css);
//			}
//		}
//		for(int i=0;i<count; i++){
//			MODELNAMESMAP cs = new MODELNAMESMAP();
//			String csss = cs.encrypt("司马");
//			if(isPrint){
//				System.out.println(csss);
//			}
//		}
		for(int i=0;i<count; i++){
			MODELNAMESKSNMAP cs = new MODELNAMESKSNMAP();
			String csss = cs.encrypt("玻馗桀  ");
			if(isPrint){
				System.out.println(csss);
			}
		}
//		for(int i=0;i<count; i++){
//			MODELNAMESMAP cs = new MODELNAMESMAP();
//			String css = cs.encrypt("牛静静");
//			if(isPrint){
//				System.out.println(css);
//			}
//		}
//		for(int i=0;i<count; i++){
//			MODELNAMESMAP cs = new MODELNAMESMAP();
//			String css = cs.encrypt("张扬");
//			if(isPrint){
//				System.out.println(css);
//			}
//		}
		
//		for(int i=0;i<count; i++){
//			MODELNAMESMAP cs = new MODELNAMESMAP();
//			String csss = cs.encrypt("ADBECFZ");
//			if(isPrint){
//				System.out.println(csss);
//			}
//		}
		for(int i=0;i<count; i++){
			MODELNAMESKSNMAP cs = new MODELNAMESKSNMAP();
			String csss = cs.encrypt("XX");
			if(isPrint){
				System.out.println("3:" + csss);
			}
		}
//		for(int i=0;i<count; i++){
//			MODELNAMESMAP cs = new MODELNAMESMAP();
//			String csss = cs.encrypt("名字");
//			if(isPrint){
//				System.out.println(csss);
//			}
//		}
//		for(int i=0;i<count; i++){
//			MODELNAMESMAP cs = new MODELNAMESMAP();
//			String csss = cs.encrypt("da..eb--fc汗");
//			if(isPrint){
//				System.out.println(csss);
//			}
//		}
//		for(int i=0;i<count; i++){
//			MODELNAMESMAP cs = new MODELNAMESMAP();
//			long csss = cs.encrypt(Long.valueOf("7897876786"));
//			if(isPrint){
//				System.out.println(csss);
//			}
//		}
//		System.out.println(df.format(new java.util.Date()));
//		for(int i=0;i<count; i++){
//			MODELNAMESMAP cs = new MODELNAMESMAP();
//			//String css = cs.encrypt("NewYork Corpration");
//			String css = cs.encrypt("苏振江");
//			if(isPrint){
//				System.out.println(css);
//			}
//		}
//		for(int i=0;i<count; i++){
//			MODELNAMESMAP cs = new MODELNAMESMAP();
//			//String css = cs.encrypt("NewYork Corpration");
//			String css = cs.encrypt("天津市23234北城ad区区ADF路楼   ");
//			if(isPrint){
//				System.out.println(css);
//			}
//		}
//		System.out.println(df.format(new java.util.Date()));
//		for(int i=0;i<count; i++){
//			MODELNAMESMAP cs = new MODELNAMESMAP();
//			String css = cs.encrypt("NewYork Corpration");
//			//String css = cs.encrypt("大连实业");
//			if(isPrint){
//				System.out.println(css);
//			}
//		}
//		System.out.println(df.format(new java.util.Date()));
//		List list = Arrays.asList(secondnames);
//		Collections.shuffle(list);
//		for(int i=0;i<list.size();i++){
//			System.out.println("'"+list.get(0)+"',");
//		}
//		List<String> list1 = new ArrayList<String>();
//		List list1 = Arrays.asList(firstnames1);
//		for(int j=0; j<list.size(); j++){
//            String str = (String)list.get(j);  //获取传入集合对象的每一个元素
//            if(!list1.contains(str)){   //查看新集合中是否有指定的元素，如果没有则加入
//                list1.add(str);
//                System.out.print("\""+str+"\","); 
//            }
//            for(int i=0;i<list1.size();i++){
//            	if(str.equals(list1.get(i))){
//            		
//            		System.out.println("\""+str+"\",");
//            		
//            	}
//            }
//        }
	}
}
