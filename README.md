# camera
# 安卓开发学习路程
安卓相机的学习与使用
# 启动Camera的两种方式
    a:直接在intent中指定应用程序的包名（前提是知道完整的包名）
    b:使用隐式intent的方式来启动包名
    //启动相机
    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    startActivity(camera);
# 拍照完成之后获取图片
## 第一种方式
直接通过data中获取，但是照片是缩略图
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
}
## 第二种方式
  a.获取文件保存路径
    private String mFilePath; // 定义sd卡路径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 获得sd卡路径
        mFilePath = Environment.getExternalStorageDirectory().getPath();
        mFilePath = mFilePath + "/" + "temp.png";
        init();
   }
   b.设置相机照片保存路径，启动相机
   Intent camera2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
   // 修改照片保存路径
   Uri photoUri = Uri.fromFile(new File(mFilePath));
   camera2.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
   //通过返回requestCode来读取相机照片，显示相机照片
   startActivityForResult(camera2, REQ_2);

   c.获取文件流，并转换为bitmap显示出来
	FileInputStream fis = null;
	try {
	    // 将文件转换成Bitmap
	    // 通过文件修改相机照片保存路径，来保存照片，并读取照片内容
	    fis = new FileInputStream(mFilePath);
	    Bitmap bitmap = BitmapFactory.decodeStream(fis);
	    iv_photo.setImageBitmap(bitmap);
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} finally {
	    try {
		fis.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}